package com.emanuelgalvao.studies.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.DeckListener

class DeckViewHolder(itemView: View, val listener: DeckListener): RecyclerView.ViewHolder(itemView) {

    private var mCardDeck: CardView = itemView.findViewById(R.id.card_deck)
    private var mTextDeckName: TextView = itemView.findViewById(R.id.text_deck_name)
    private var mImageFavorite: ImageView = itemView.findViewById(R.id.image_favorite)

    fun bind(deck: Deck) {

        mTextDeckName.text = deck.name

        if (deck.favorite) {
            mImageFavorite.setImageDrawable(itemView.resources.getDrawable(R.drawable.ic_star_filled))
        } else {
            mImageFavorite.setImageDrawable(itemView.resources.getDrawable(R.drawable.ic_star_border))
        }

        mCardDeck.setOnClickListener {
            listener.onClick(deck)
        }

        mImageFavorite.setOnClickListener {
            if (!deck.favorite) {
                mImageFavorite.setImageDrawable(itemView.resources.getDrawable(R.drawable.ic_star_filled))
            } else {
                mImageFavorite.setImageDrawable(itemView.resources.getDrawable(R.drawable.ic_star_border))
            }
            listener.onFavorite(deck)
        }
    }
}