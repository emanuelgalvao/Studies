package com.emanuelgalvao.studies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.studies.model.User
import com.emanuelgalvao.studies.service.repository.UserRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val mUserRepository = UserRepository(application)

    private val mUser = MutableLiveData<User>()
    val user: LiveData<User> = mUser

    fun getUser() {
        mUser.value = User("", mUserRepository.getUserName(), mUserRepository.getEmail())
    }

    fun logout() {
        mUserRepository.logout()
    }
}