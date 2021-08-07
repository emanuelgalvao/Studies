package com.emanuelgalvao.studies.ui.activity

import android.animation.Animator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        binding.progress.isVisible = true
        binding.textProgress.isVisible = true
        binding.flipView.isVisible = false
        binding.linearButtons.isVisible = false

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
            .setNegativeButton("Não") { _, _ -> }

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
        mViewModel.updateCard(mDeckId, correct)
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
            } else {
                binding.textFront.text = it.frontPhrase
                binding.textBack.text = it.backPhrase
            }
        })

        mViewModel.showResult.observe(this, {
            if (it) {
                val result = mViewModel.result.value
                val bundle = Bundle()
                bundle.putSerializable("result", result)
                bundle.putString("deckId", mDeckId)
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun startStudies() {
        mViewModel.getRandomCard()
        binding.progress.isVisible = false
        binding.textProgress.isVisible = false
        binding.flipView.isVisible = true
        binding.linearButtons.isVisible = true
    }
}