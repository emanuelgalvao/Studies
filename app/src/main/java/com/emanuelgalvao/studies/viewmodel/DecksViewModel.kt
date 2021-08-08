package com.emanuelgalvao.studies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.listener.ValidationListener
import com.emanuelgalvao.studies.service.repository.DeckRepository

class DecksViewModel : ViewModel() {

    private val mDeckRepository = DeckRepository()

    private var mDeckList = MutableLiveData<List<Deck>>()
    val deckList: LiveData<List<Deck>> = mDeckList

    private var mCreateDeck = MutableLiveData<ValidationListener>()
    val createDeck: LiveData<ValidationListener> = mCreateDeck

    private var mUpdateDeck = MutableLiveData<ValidationListener>()
    val updateDeck: LiveData<ValidationListener> = mUpdateDeck

    private var mDeleteDeck = MutableLiveData<ValidationListener>()
    val deleteDeck: LiveData<ValidationListener> = mDeleteDeck

    fun getAllDecks() {
        mDeckRepository.getAllDecks(object: AsyncTaskListener<List<Deck>> {
            override fun onSucess(model: List<Deck>) {
                mDeckList.value = model
            }

            override fun onFailure(message: String) {
            }

        })
    }

    fun createDeck(deckName: String) {
        mDeckRepository.createDeck(deckName, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllDecks()
                mCreateDeck.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mCreateDeck.value = ValidationListener(message)
            }

        })
    }

    fun updateDeck(deck: Deck) {
        mDeckRepository.updateDeck(deck, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllDecks()
                mUpdateDeck.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mUpdateDeck.value = ValidationListener(message)
            }
        })
    }

    fun deleteDeck(deck: Deck) {
        mDeckRepository.deleteDeck(deck, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllDecks()
                mDeleteDeck.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mDeleteDeck.value = ValidationListener(message)
            }

        })
    }
}