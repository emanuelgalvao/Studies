package com.emanuelgalvao.studies.service.listener

interface AsyncTaskListener<T> {

    fun onSucess(model: T)

    fun onFailure(message: String)
}