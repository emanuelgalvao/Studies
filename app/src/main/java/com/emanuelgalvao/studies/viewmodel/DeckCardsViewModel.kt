package com.emanuelgalvao.studies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.listener.ValidationListener
import com.emanuelgalvao.studies.service.repository.CardRepository

class DeckCardsViewModel : ViewModel() {

    private val mCardRepository = CardRepository()

    private var mCardList = MutableLiveData<List<Card>>()
    val cardList: LiveData<List<Card>> = mCardList

    private var mCreateCard = MutableLiveData<ValidationListener>()
    val createCard: LiveData<ValidationListener> = mCreateCard

    private var mUpdateCard = MutableLiveData<ValidationListener>()
    val updateCard: LiveData<ValidationListener> = mUpdateCard

    private var mDeleteCard = MutableLiveData<ValidationListener>()
    val deleteCard: LiveData<ValidationListener> = mDeleteCard

    fun getAllCards(deckId: String) {
        mCardRepository.getAllCards(deckId, object: AsyncTaskListener<List<Card>> {
            override fun onSucess(model: List<Card>) {
                mCardList.value = model
            }

            override fun onFailure(message: String) {
            }
        })
    }

    fun createCard(deckId: String, frontPhrase: String, backPhrase: String) {
        mCardRepository.createCard(deckId, Card("", frontPhrase, backPhrase, 0, 0), object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllCards(deckId)
                mCreateCard.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mCreateCard.value = ValidationListener(message)
            }

        })
    }

    fun updateCard(deckId: String, card: Card) {
        mCardRepository.updateCard(deckId, card, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllCards(deckId)
                mUpdateCard.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mUpdateCard.value = ValidationListener(message)
            }
        })
    }

    fun deleteCard(deckId: String, card: Card) {
        mCardRepository.deleteCard(deckId, card, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {
                getAllCards(deckId)
                mDeleteCard.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mDeleteCard.value = ValidationListener(message)
            }

        })
    }
}