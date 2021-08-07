package com.emanuelgalvao.studies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.model.StudyResult
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.repository.CardRepository
import kotlin.random.Random

class CardsViewModel: ViewModel()  {

    private val mCardRepository = CardRepository()

    private var mCardList = MutableLiveData<List<Card>>()
    val cardList: LiveData<List<Card>> = mCardList

    private var mNextCard = MutableLiveData<Card>()
    val nextCard: LiveData<Card> = mNextCard

    private var mShowResult = MutableLiveData<Boolean>()
    var showResult: LiveData<Boolean> = mShowResult

    private var mResult = MutableLiveData<StudyResult>()
    var result: LiveData<StudyResult> = mResult

    private lateinit var mList: MutableList<Card>

    fun getAllCards(deckId: String) {
        mCardRepository.getAllCards(deckId, object: AsyncTaskListener<List<Card>> {
            override fun onSucess(model: List<Card>) {
                mCardList.value = model
                mResult.value = StudyResult()
                mResult.value!!.cardsNumber = model.size
            }

            override fun onFailure(message: String) {
            }
        })
    }

    fun getRandomCard() {
        mList = mCardList.value!!.toMutableList()

        if (mList.count() > 0) {
            val index: Int = Random.nextInt(0, mList.size)
            mNextCard.value = mList[index]
            mList.removeAt(index)
            mCardList.value = mList
        } else {
            mShowResult.value = true
        }
    }

    fun updateCard(deckId: String, correct: Boolean) {
        val card: Card = mNextCard.value!!
        card.displayTimesNumber = card.displayTimesNumber + 1

        if (correct) {
            card.correctTimesNumber = card.correctTimesNumber + 1

            val resultUpdate: StudyResult? = mResult.value
            if (resultUpdate != null) {
                resultUpdate.correctCardsNumber = resultUpdate.correctCardsNumber + 1
                mResult.value = resultUpdate
            }
        }

        mCardRepository.updateCard(deckId, card, object: AsyncTaskListener<Boolean> {
            override fun onSucess(model: Boolean) {}
            override fun onFailure(message: String) {}
        })

        getRandomCard()
    }
}