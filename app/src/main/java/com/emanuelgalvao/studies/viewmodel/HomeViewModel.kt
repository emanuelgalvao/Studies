package com.emanuelgalvao.studies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.repository.DeckRepository
import com.emanuelgalvao.studies.service.repository.UserRepository

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val mDeckRepository = DeckRepository()
    private val mUserRepository = UserRepository(application)

    private var mDeckList = MutableLiveData<List<Deck>>()
    val deckList: LiveData<List<Deck>> = mDeckList

    private val mUsername = MutableLiveData<String>()
    val username: LiveData<String> = mUsername

    fun getFavoriteDecks() {
        mDeckRepository.getFavoriteDecks(object: AsyncTaskListener<List<Deck>> {
            override fun onSucess(model: List<Deck>) {
                mDeckList.value = model
            }

            override fun onFailure(message: String) {
            }

        })
    }

    fun getUsername() {
        mUsername.value = mUserRepository.getUserName().split(" ")[0]
    }
}