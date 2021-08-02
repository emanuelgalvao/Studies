package com.emanuelgalvao.studies.service.listener

import com.emanuelgalvao.studies.model.Deck

interface DeckListener {

    fun onFavorite(deck: Deck)

    fun onClick(deck: Deck)
}