package com.emanuelgalvao.studies.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var mViewModel: SplashViewModel
    private var logged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        observers()
        mViewModel.verifySignedInUser()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent: Intent = if (logged) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun observers() {
        mViewModel.logged.observe(this) {
            if (it.isSucess()) {
                logged = true
            }
        }
    }
}