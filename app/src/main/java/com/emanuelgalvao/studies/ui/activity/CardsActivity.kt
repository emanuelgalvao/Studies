package com.emanuelgalvao.studies.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityCardsBinding
import com.emanuelgalvao.studies.viewmodel.CardsViewModel

class CardsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: CardsViewModel
    private lateinit var binding: ActivityCardsBinding

    private lateinit var mDeckId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mDeckId = intent.extras!!.getString("deckId").toString()

        mViewModel = ViewModelProvider(this).get(CardsViewModel::class.java)

        mViewModel.getAllCards(mDeckId)

        listeners()
        observers()
    }

    override fun onBackPressed() {
        openConfirmDialog()
    }

    private fun openConfirmDialog() {

        val builder = AlertDialog.Builder(this)
            .setTitle("SAIR")
            .setMessage("Tem certeza que deseja cancelar o estudo?")
            .setPositiveButton("Sim") { _, _ -> run { finish() } }
            .setNeutralButton("Não") { _, _ -> }

        builder.show()
    }

    private fun listeners() {
        binding.imageCorrect.setOnClickListener(this)
        binding.imageError.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.image_correct -> answerClick(true)
            R.id.image_error -> answerClick(false)
        }
    }

    private fun answerClick(correct: Boolean) {
        mViewModel.getRandomCard()
    }

    private fun observers() {
        mViewModel.cardList.observe(this, {
            if (it.count() > 0) {
                startStudies()
                mViewModel.cardList.removeObservers(this)
            }
        })

        mViewModel.nextCard.observe(this, {

            if (binding.flipView.isBackSide) {
                binding.flipView.flipTheView()

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.textFront.text = it.frontPhrase
                    binding.textBack.text = it.backPhrase
                }, 400)
            }
        })
    }

    private fun startStudies() {
        mViewModel.getRandomCard()
    }
}