package com.emanuelgalvao.studies.service.repository

import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.model.DeckFirebase
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DeckRepository {

    private val mFirebaseAuth: FirebaseAuth = Firebase.auth
    private val mFirebaseDatabase = Firebase.database.reference

    fun getAllDecks(listener: AsyncTaskListener<List<Deck>>) {
        mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).get()
            .addOnCompleteListener { it ->

                val deckList: MutableList<Deck> = arrayListOf()

                it.result!!.children.forEach {
                    val deck = Deck(it.key!!, (it.value as HashMap<*, *>)["name"]!! as String, (it.value as HashMap<*, *>)["favorite"]!! as Boolean)
                    deckList.add(deck)
                }
                listener.onSucess(deckList)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }

    fun createDeck(deckName: String, listener: AsyncTaskListener<Boolean>) {
        mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).child(mFirebaseDatabase.push().key.toString()).setValue(DeckFirebase(deckName, false))
            .addOnSuccessListener {
                listener.onSucess(true)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }

    fun updateDeck(deck: Deck, listener: AsyncTaskListener<Boolean>) {
        mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).child(deck.id).setValue(DeckFirebase(deck.name, deck.favorite))
            .addOnSuccessListener {
                listener.onSucess(true)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }
}