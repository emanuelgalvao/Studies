package com.emanuelgalvao.studies.service.listener

import com.emanuelgalvao.studies.model.Card

interface CardListener {

    fun onEdit(card: Card)

}