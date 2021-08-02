package com.emanuelgalvao.studies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emanuelgalvao.studies.model.User
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.listener.ValidationListener
import com.emanuelgalvao.studies.service.repository.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application)  {

    private val mUserRepository = UserRepository(application)

    private val mRegister = MutableLiveData<ValidationListener>()
    val register: LiveData<ValidationListener> = mRegister

    fun register(name: String, email: String, password: String) {

        if (name != "" && email != "" && password != "") {

            mUserRepository.register(name, email, password, object: AsyncTaskListener<User> {
                override fun onSucess(model: User) {
                    mRegister.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mRegister.value = ValidationListener(message)
                }
            })

        } else if (name == "") {
            mRegister.value = ValidationListener("Preencha o nome.")
        } else if (email == "") {
            mRegister.value = ValidationListener("Preencha o email.")
        } else {
            mRegister.value = ValidationListener("Preencha a senha.")
        }
    }
}