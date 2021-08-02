package com.emanuelgalvao.studies.service.repository

import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.model.CardFirebase
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CardRepository {

    private val mFirebaseAuth: FirebaseAuth = Firebase.auth
    private val mFirebaseDatabase = Firebase.database.reference

    fun getAllCards(deckId: String, listener: AsyncTaskListener<List<Card>>) {
        mFirebaseDatabase.child("cards").child(mFirebaseAuth.currentUser?.uid.toString()).child(deckId).get()
            .addOnCompleteListener { it ->

                val cardList: MutableList<Card> = arrayListOf()

                it.result!!.children.forEach {
                    val card = Card(it.key!!, (it.value as HashMap<*, *>)["frontPhrase"]!! as String, (it.value as HashMap<*, *>)["backPhrase"]!! as String, (it.value as HashMap<*, *>)["displayTimesNumber"]!! as Long, (it.value as HashMap<*, *>)["correctTimesNumber"]!! as Long)
                    cardList.add(card)
                }
                listener.onSucess(cardList)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }

    fun createCard(deckId: String, card: Card, listener: AsyncTaskListener<Boolean>) {
        mFirebaseDatabase.child("cards").child(mFirebaseAuth.currentUser?.uid.toString()).child(deckId).child(mFirebaseDatabase.push().key.toString()).setValue(
            CardFirebase(card.frontPhrase, card.backPhrase, card.displayTimesNumber, card.correctTimesNumber))
            .addOnSuccessListener {
                listener.onSucess(true)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }

    fun updateCard(deckId: String, card: Card, listener: AsyncTaskListener<Boolean>) {
        mFirebaseDatabase.child("cards").child(mFirebaseAuth.currentUser?.uid.toString()).child(deckId).child(card.id).setValue(
            CardFirebase(card.frontPhrase, card.backPhrase, card.displayTimesNumber, card.correctTimesNumber))
            .addOnSuccessListener {
                listener.onSucess(true)
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }
}