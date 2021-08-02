package com.emanuelgalvao.studies.model

import java.io.Serializable

data class Card(
    val id: String,
    val frontPhrase: String,
    val backPhrase: String,
    val displayTimesNumber: Long,
    val correctTimesNumber: Long
)

data class CardFirebase(
    val frontPhrase: String,
    val backPhrase: String,
    val displayTimesNumber: Long,
    val correctTimesNumber: Long
)
