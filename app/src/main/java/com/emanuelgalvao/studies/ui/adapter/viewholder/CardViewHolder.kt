package com.emanuelgalvao.studies.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.service.listener.CardListener

class CardViewHolder(itemView: View, val listener: CardListener): RecyclerView.ViewHolder(itemView) {

    private var mTextCardFront: TextView = itemView.findViewById(R.id.text_card_front)
    private var mTextCardStatistics: TextView = itemView.findViewById(R.id.text_card_statistics)
    private var mImageEdit: ImageView = itemView.findViewById(R.id.image_edit)

    fun bind(card: Card) {

        mTextCardFront.text = card.frontPhrase

        if (card.displayTimesNumber > 0) {
            mTextCardStatistics.isVisible = true
            mTextCardStatistics.text = "Exibições: ${card.displayTimesNumber} - Acertos: ${card.correctTimesNumber}"
        } else {
            mTextCardStatistics.isVisible = false
        }

        mImageEdit.setOnClickListener {
            listener.onEdit(card)
        }
    }
}