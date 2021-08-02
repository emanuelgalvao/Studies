package com.emanuelgalvao.studies.model

import java.io.Serializable

data class Deck(
    val id: String,
    val name: String,
    var favorite: Boolean
): Serializable

data class DeckFirebase(
    val name: String,
    val favorite: Boolean
)
