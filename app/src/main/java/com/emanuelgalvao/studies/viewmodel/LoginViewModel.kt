package com.emanuelgalvao.studies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emanuelgalvao.studies.model.User
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.listener.ValidationListener
import com.emanuelgalvao.studies.service.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application)  {

    private val mUserRepository = UserRepository(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    val login: LiveData<ValidationListener> = mLogin

    fun login(email: String, password: String) {

        if (email != "" && password != "") {

            mUserRepository.login(email, password, object: AsyncTaskListener<User> {
                override fun onSucess(model: User) {
                    mLogin.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mLogin.value = ValidationListener(message)
                }
            })

        } else if (email == "") {
            mLogin.value = ValidationListener("Preencha o email.")
        } else {
            mLogin.value = ValidationListener("Preencha a senha.")
        }
    }
}