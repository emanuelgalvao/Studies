package com.emanuelgalvao.studies.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.FragmentDeckCardsBinding
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.service.listener.CardListener
import com.emanuelgalvao.studies.ui.activity.CardsActivity
import com.emanuelgalvao.studies.ui.activity.MainActivity
import com.emanuelgalvao.studies.ui.adapter.CardAdapter
import com.emanuelgalvao.studies.util.AlertUtils
import com.emanuelgalvao.studies.viewmodel.DeckCardsViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DeckCardsFragment : Fragment(), View.OnClickListener {

    private val mFragmentArgs by navArgs<DeckCardsFragmentArgs>()

    private var _binding: FragmentDeckCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: DeckCardsViewModel

    private lateinit var mDeckListener: CardListener
    private val mAdapter = CardAdapter()

    private lateinit var mDeckId: String

    private lateinit var mSelectedCard: Card

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentDeckCardsBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(this).get(DeckCardsViewModel::class.java)

        mDeckId = mFragmentArgs.deck.id
        (activity as MainActivity).supportActionBar?.title = mFragmentArgs.deck.name

        binding.buttonStart.isVisible = false
        binding.buttonAddCard.isVisible = false

        val recycler = binding.recyclerCards
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        mDeckListener = object: CardListener {
            override fun onEdit(card: Card) {
                editCard(card)
            }
        }

        listeners()
        observers()

        return view
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmerAnimation()
        mViewModel.getAllCards(mDeckId)
        mAdapter.attachListener(mDeckListener)
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmerAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.buttonAddCard.setOnClickListener(this)
        binding.buttonStart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_card -> openCreateCard()
            R.id.button_start -> startStudies()
        }
    }

    @SuppressLint("CheckResult")
    private fun openCreateCard() {
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.new_card)
            customView(R.layout.bottom_sheet_create_card)
            positiveButton(null, "Criar") {
                noAutoDismiss()

                val frontPhrase = getCustomView().findViewById<TextInputEditText>(R.id.edit_front).text.toString().trim()
                val backPhrase = getCustomView().findViewById<TextInputEditText>(R.id.edit_back).text.toString().trim()

                if (frontPhrase != "" && backPhrase != "") {
                    createCard(frontPhrase, backPhrase)
                    dismiss()
                } else if (frontPhrase == "") {
                    getCustomView().findViewById<TextInputLayout>(R.id.textfield_front).error = "Preencha o texto da frente de cartão."
                } else {
                    getCustomView().findViewById<TextInputLayout>(R.id.textfield_back).error = "Preencha o texto de trás do cartão."
                }
            }
            negativeButton(null, "Cancelar")
        }
    }

    private fun createCard(frontPhrase: String, backPhrase: String) {
        AlertUtils.showProgressDialog(requireContext(), "Criando Carta...")
        mViewModel.createCard(mDeckId, frontPhrase, backPhrase)
    }

    private fun editCard(card: Card) {

        mSelectedCard = card

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.edit_card)
            customView(R.layout.bottom_sheet_create_card)
            getCustomView().findViewById<TextInputEditText>(R.id.edit_front).setText(card.frontPhrase)
            getCustomView().findViewById<TextInputEditText>(R.id.edit_back).setText(card.backPhrase)
            getCustomView().findViewById<Button>(R.id.button_delete).isVisible = true
            getCustomView().findViewById<Button>(R.id.button_delete).setOnClickListener {
                dismiss()
                deleteCard()
            }
            positiveButton(null, "Salvar") {
                noAutoDismiss()

                val frontPhrase = getCustomView().findViewById<TextInputEditText>(R.id.edit_front).text.toString().trim()
                val backPhrase = getCustomView().findViewById<TextInputEditText>(R.id.edit_back).text.toString().trim()

                if (frontPhrase != "" && backPhrase != "") {
                    updateCard(Card(card.id, frontPhrase, backPhrase, card.displayTimesNumber, card.correctTimesNumber))
                    dismiss()
                } else if (frontPhrase == "") {
                    getCustomView().findViewById<TextInputLayout>(R.id.textfield_front).error = "Preencha o texto da frente de cartão."
                } else {
                    getCustomView().findViewById<TextInputLayout>(R.id.textfield_back).error = "Preencha o texto de trás do cartão."
                }
            }
            negativeButton(null, "Cancelar")
        }
    }

    private fun updateCard(card: Card) {
        AlertUtils.showProgressDialog(requireContext(), "Alterando Carta...")
        mViewModel.updateCard(mDeckId, card)
    }

    private fun deleteCard() {
        AlertUtils.showProgressDialog(requireContext(), "Excluindo Carta...")
        mViewModel.deleteCard(mDeckId, mSelectedCard)
    }

    private fun startStudies() {
        val intent = Intent(activity, CardsActivity::class.java)
        val bundle = Bundle()
        bundle.putString("deckId", mDeckId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun observers() {
        mViewModel.cardList.observe(viewLifecycleOwner, {
            if (it.count() >= 0) {
                mAdapter.updateList(it)

                binding.buttonStart.isVisible = it.count() > 0
                binding.buttonAddCard.isVisible = true
                binding.shimmerLayout.stopShimmerAnimation()
                binding.shimmerLayout.visibility = View.GONE
                binding.recyclerCards.visibility = View.VISIBLE
            }
        })

        mViewModel.createCard.observe(viewLifecycleOwner, {
            if (it.isSucess()) {
                AlertUtils.showSnackbar(binding.root, "Carta criada com sucesso!", ContextCompat.getColor(requireContext(), R.color.snack_green))
            } else {
                AlertUtils.showSnackbar(binding.root, it.getMessage(), ContextCompat.getColor(requireContext(), R.color.snack_red))
            }
            AlertUtils.closeProgressDialog()
        })

        mViewModel.updateCard.observe(viewLifecycleOwner, {
            if (it.isSucess()) {
                AlertUtils.showSnackbar(binding.root, "Carta alterada com sucesso!", ContextCompat.getColor(requireContext(), R.color.snack_green))
            } else {
                AlertUtils.showSnackbar(binding.root, it.getMessage(), ContextCompat.getColor(requireContext(), R.color.snack_red))
            }
            AlertUtils.closeProgressDialog()
        })

        mViewModel.deleteCard.observe(viewLifecycleOwner, {
            if (it.isSucess()) {
                AlertUtils.showSnackbar(binding.root, "Carta excluída com sucesso!", ContextCompat.getColor(requireContext(), R.color.snack_green))
            } else {
                AlertUtils.showSnackbar(binding.root, it.getMessage(), ContextCompat.getColor(requireContext(), R.color.snack_red))
            }
            AlertUtils.closeProgressDialog()
        })
    }

}