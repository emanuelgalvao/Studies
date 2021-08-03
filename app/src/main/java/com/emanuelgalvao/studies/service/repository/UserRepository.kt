package com.emanuelgalvao.studies.service.repository

import android.content.Context
import com.emanuelgalvao.studies.model.User
import com.emanuelgalvao.studies.service.listener.AsyncTaskListener
import com.emanuelgalvao.studies.service.repository.local.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository(val context: Context) {

    private val mFirebaseAuth: FirebaseAuth = Firebase.auth
    private val mFirebaseDatabase = Firebase.database.reference
    private val mSharedPreferences = SharedPreferences(context)

    fun verifySignedInUser(listener: AsyncTaskListener<User>) {

        val currentUser = mFirebaseAuth.currentUser

        if(currentUser != null){
            val user = User(currentUser.uid, currentUser.email.toString(), mSharedPreferences.get("name"))

            listener.onSucess(user)
        } else {
            listener.onFailure("Usuário não logado.")
        }
    }

    fun getUserName(): String = mSharedPreferences.get("name")

    fun getEmail(): String = mSharedPreferences.get("email")

    fun login(email: String, password: String, listener: AsyncTaskListener<User>) {

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mFirebaseDatabase.child("users").child(mFirebaseAuth.currentUser?.uid.toString()).child("name").get().addOnCompleteListener {

                    val user = User(mFirebaseAuth.currentUser?.uid.toString(), mFirebaseAuth.currentUser?.email.toString(), it.result?.value.toString())

                    mSharedPreferences.store("uid", user.uid)
                    mSharedPreferences.store("email", user.email)
                    mSharedPreferences.store("name", user.name)

                    listener.onSucess(user)
                }
            } else {
                listener.onFailure(task.exception?.message.toString())
            }
        }
    }

    fun register(name: String, email: String, password: String, listener: AsyncTaskListener<User>) {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = User(mFirebaseAuth.currentUser?.uid.toString(), mFirebaseAuth.currentUser?.email.toString(), name)

                mFirebaseDatabase.child("users").child(user.uid).child("name").setValue(name).addOnCompleteListener {
                    mSharedPreferences.store("uid", user.uid)
                    mSharedPreferences.store("email", user.email)
                    mSharedPreferences.store("name", user.name)
                    listener.onSucess(user)
                }
            } else {
                listener.onFailure(task.exception?.message.toString())
            }
        }
    }

    fun updateName(name: String, listener: AsyncTaskListener<User>) {
        val currentUser = mFirebaseAuth.currentUser

        val user = User(currentUser?.uid.toString(), currentUser?.email.toString(), name)

        if (name != "") {
            mFirebaseDatabase.child("users").child(currentUser!!.uid).child("name").setValue(name).addOnCompleteListener {
                mSharedPreferences.store("name", name)
                listener.onSucess(user)
            }
        } else {
            listener.onFailure("Ocorreu um erro ao atualizar os dados. Tente novamente.")
        }
    }

    fun logout() {
        mSharedPreferences.remove("uid")
        mSharedPreferences.remove("email")
        mSharedPreferences.remove("name")
        mFirebaseAuth.signOut()
    }
}