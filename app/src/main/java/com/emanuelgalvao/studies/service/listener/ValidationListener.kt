package com.emanuelgalvao.studies.service.listener

class ValidationListener(message: String = "") {

    private var mSucess: Boolean = true
    private var mMessage: String = ""

    init {
        if(message != "") {
            mSucess = false
            mMessage = message
        }
    }

    fun isSucess() = mSucess

    fun getMessage() = mMessage
}