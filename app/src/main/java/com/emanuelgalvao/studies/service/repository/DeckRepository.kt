package com.emanuelgalvao.studies.service.repository

import com.emanuelgalvao.studies.model.Card
import com.emanuelgalvao.studies.model.Deck
import com.emanuelgalvao.studies.model.DeckFirebase
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DeckRepository {

    private val mFirebaseAuth: FirebaseAuth = Firebase.auth
    private val mFirebaseDatabase = Firebase.database.reference

    fun getAllDecks(listener: AsyncTaskListener<List<Deck>>) {

        val decksFirebase = mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).orderByChild("favorite")

        val deckList: MutableList<Deck> = arrayListOf()

        decksFirebase.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val deck = Deck(snapshot.key!!, (snapshot.value as HashMap<*, *>)["name"]!! as String, (snapshot.value as HashMap<*, *>)["favorite"]!! as Boolean)
                deckList.add(deck)
                listener.onSucess(deckList.asReversed())
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun getFavoriteDecks(listener: AsyncTaskListener<List<Deck>>) {

        val decksFirebase = mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).orderByChild("favorite").equalTo(true)

        val deckList: MutableList<Deck> = arrayListOf()

        decksFirebase.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val deck = Deck(snapshot.key!!, (snapshot.value as HashMap<*, *>)["name"]!! as String, (snapshot.value as HashMap<*, *>)["favorite"]!! as Boolean)
                deckList.add(deck)
                listener.onSucess(deckList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })
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

    fun deleteDeck(deck: Deck, listener: AsyncTaskListener<Boolean>) {
        mFirebaseDatabase.child("decks").child(mFirebaseAuth.currentUser?.uid.toString()).child(deck.id).removeValue()
            .addOnSuccessListener {
                mFirebaseDatabase.child("cards").child(mFirebaseAuth.currentUser?.uid.toString()).child(deck.id).removeValue()
                    .addOnSuccessListener {
                        listener.onSucess(true)
                    }
                    .addOnFailureListener {
                        listener.onFailure(it.message.toString())
                    }
            }
            .addOnFailureListener {
                listener.onFailure(it.message.toString())
            }
    }
}