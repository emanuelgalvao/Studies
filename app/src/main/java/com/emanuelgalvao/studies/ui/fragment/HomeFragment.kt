package com.emanuelgalvao.studies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.FragmentHomeBinding
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.DeckListener
import com.emanuelgalvao.studies.ui.adapter.DeckAdapter
import com.emanuelgalvao.studies.viewmodel.HomeViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: HomeViewModel

    private lateinit var mDeckListener: DeckListener
    private val mAdapter = DeckAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.progress.isVisible = true
        binding.textProgress.isVisible = true

        mAdapter.setShowActions(false)

        val recycler = binding.recyclerDecks
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        mDeckListener = object: DeckListener {
            override fun onFavorite(deck: Deck) {}
            override fun onEdit(deck: Deck) {}

            override fun onClick(deck: Deck) {
                val action = HomeFragmentDirections.actionNavHomeToNavDeckCards(deck)
                findNavController().navigate(action)
            }
        }

        mViewModel.getUsername()

        listeners()
        observers()

        return view
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getFavoriteDecks()
        mAdapter.attachListener(mDeckListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.buttonTimer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.button_timer -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavPomodoro())
        }
    }

    private fun observers() {
        mViewModel.username.observe(viewLifecycleOwner, {
            binding.textHello.text = "Olá, ${it}!"
        })

        mViewModel.deckList.observe(viewLifecycleOwner, {
            if (it.count() >= 0) {
                mAdapter.updateList(it)
                binding.progress.isVisible = false
                binding.textProgress.isVisible = false
            }
        })
    }
}