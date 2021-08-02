package com.emanuelgalvao.studies.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Card

class CardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        supportActionBar?.hide()
    }
}