package com.emanuelgalvao.studies.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.emanuelgalvao.studies.databinding.DeckCardsFragmentBinding
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.service.listener.CardListener
import com.emanuelgalvao.studies.ui.activity.MainActivity
import com.emanuelgalvao.studies.ui.adapter.CardAdapter
import com.emanuelgalvao.studies.util.AlertUtils
import com.emanuelgalvao.studies.viewmodel.DeckCardsViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DeckCardsFragment : Fragment(), View.OnClickListener {

    private val mFragmentArgs by navArgs<DeckCardsFragmentArgs>()

    private var _binding: DeckCardsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: DeckCardsViewModel

    private lateinit var mDeckListener: CardListener
    private val mAdapter = CardAdapter()

    private lateinit var mDeckId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = DeckCardsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(this).get(DeckCardsViewModel::class.java)

        mDeckId = mFragmentArgs.deck.id
        (activity as MainActivity).supportActionBar?.title = mFragmentArgs.deck.name

        val recycler = binding.recyclerCards
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        mDeckListener = object: CardListener {
            override fun onEdit(card: Card) {

            }
        }

        listeners()
        observers()

        return view
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllCards(mDeckId)
        mAdapter.attachListener(mDeckListener)
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

    private fun startStudies() {

    }

    private fun observers() {
        mViewModel.cardList.observe(viewLifecycleOwner, {
            if (it.count() >= 0) {
                mAdapter.updateList(it)
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

    }

}