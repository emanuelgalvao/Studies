package com.emanuelgalvao.studies.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.FragmentDecksBinding
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.DeckListener
import com.emanuelgalvao.studies.ui.adapter.DeckAdapter
import com.emanuelgalvao.studies.util.AlertUtils
import com.emanuelgalvao.studies.viewmodel.DecksViewModel

class DecksFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDecksBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: DecksViewModel

    private lateinit var mDeckListener: DeckListener
    private val mAdapter = DeckAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        _binding = FragmentDecksBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(this).get(DecksViewModel::class.java)


        val recycler = binding.recyclerDecks
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        mDeckListener = object: DeckListener {
            override fun onFavorite(deck: Deck) {
                deck.favorite = !deck.favorite
                mViewModel.updateDeck(deck)
            }

            override fun onClick(deck: Deck) {
                val action = DecksFragmentDirections.actionNavDecksToNavDeckCards(deck)
                findNavController().navigate(action)

            }

        }

        listeners()
        observers()

        return view
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllDecks()
        mAdapter.attachListener(mDeckListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.buttonAddDeck.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_deck -> openCreateDeck()
        }
    }

    @SuppressLint("CheckResult")
    private fun openCreateDeck() {
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.new_deck)
            input(hintRes = R.string.name, waitForPositiveButton = true) { dialog, text ->
                val inputField = dialog.getInputField()
                val isValid = text.isNotEmpty()

                inputField.error = if (isValid) null else "Preencha o nome do baralho!"
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
            }
            positiveButton(null, "Criar") {
                //noAutoDismiss()
                createDeck(getInputField().text.toString())
            }
            negativeButton(null, "Cancelar")
        }
    }

    private fun createDeck(deckName: String) {
        AlertUtils.showProgressDialog(requireContext(), "Criando Baralho...")
        mViewModel.createDeck(deckName)
    }

    private fun observers() {
        mViewModel.deckList.observe(viewLifecycleOwner, {
            if (it.count() >= 0) {
                mAdapter.updateList(it)
            }
        })

        mViewModel.createDeck.observe(viewLifecycleOwner, {
            if (it.isSucess()) {
                AlertUtils.showSnackbar(binding.root, "Baralho criado com sucesso!", ContextCompat.getColor(requireContext(), R.color.snack_green))
            } else {
                AlertUtils.showSnackbar(binding.root, it.getMessage(), ContextCompat.getColor(requireContext(), R.color.snack_red))
            }
            AlertUtils.closeProgressDialog()
        })

        mViewModel.updateDeck.observe(viewLifecycleOwner, {
            if (!it.isSucess()) {
                AlertUtils.showSnackbar(binding.root, it.getMessage(), ContextCompat.getColor(requireContext(), R.color.snack_red))
            }
        })
    }

}