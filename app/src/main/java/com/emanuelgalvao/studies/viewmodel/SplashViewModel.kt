package com.emanuelgalvao.studies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emanuelgalvao.studies.model.User
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.listener.ValidationListener
import com.emanuelgalvao.studies.service.repository.UserRepository

class SplashViewModel(application: Application): AndroidViewModel(application) {

    private val mUserRepository = UserRepository(application)

    private val mLogged = MutableLiveData<ValidationListener>()
    val logged: LiveData<ValidationListener> = mLogged

    fun verifySignedInUser() {
        mUserRepository.verifySignedInUser(object : AsyncTaskListener<User>{
            override fun onSucess(model: User) {
                mLogged.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mLogged.value = ValidationListener("Não logado")
            }

        })
    }

}