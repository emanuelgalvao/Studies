package com.emanuelgalvao.studies.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.databinding.ActivitySplashBinding
import com.emanuelgalvao.studies.util.ElementUtils
import com.emanuelgalvao.studies.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var mViewModel: SplashViewModel
    private var logged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ElementUtils.selectRandomBackground()
        binding.imageBackground.setImageResource(ElementUtils.getSelectedBackground())

        mViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        observers()
        mViewModel.verifySignedInUser()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent: Intent = if (logged) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, ViewPagerActivity::class.java)
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