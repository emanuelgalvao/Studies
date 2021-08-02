package com.emanuelgalvao.studies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.DeckListener
import com.emanuelgalvao.studies.ui.adapter.viewholder.DeckViewHolder

class DeckAdapter: RecyclerView.Adapter<DeckViewHolder>() {

    private var mDeckList: List<Deck> = arrayListOf()
    private lateinit var mDeckListener: DeckListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.adapter_deck, parent, false)
        return DeckViewHolder(item, mDeckListener)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        holder.bind(mDeckList[position])
    }

    override fun getItemCount(): Int {
        return mDeckList.count()
    }

    fun attachListener(listener: DeckListener) {
        mDeckListener = listener
    }

    fun updateList(list: List<Deck>) {
        mDeckList = list
        notifyDataSetChanged()
    }
}