package com.emanuelgalvao.studies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.CardListener
import com.emanuelgalvao.studies.service.listener.DeckListener
import com.emanuelgalvao.studies.ui.adapter.viewholder.CardViewHolder
import com.emanuelgalvao.studies.ui.adapter.viewholder.DeckViewHolder

class CardAdapter: RecyclerView.Adapter<CardViewHolder>() {

    private var mCardList: List<Card> = arrayListOf()
    private lateinit var mCardListener: CardListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.adapter_card, parent, false)
        return CardViewHolder(item, mCardListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(mCardList[position])
    }

    override fun getItemCount(): Int {
        return mCardList.count()
    }

    fun attachListener(listener: CardListener) {
        mCardListener = listener
    }

    fun updateList(list: List<Card>) {
        mCardList = list
        notifyDataSetChanged()
    }
}