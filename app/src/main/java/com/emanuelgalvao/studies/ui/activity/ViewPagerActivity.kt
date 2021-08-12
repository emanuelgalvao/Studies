package com.emanuelgalvao.studies.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityViewPagerBinding
import com.emanuelgalvao.studies.model.Tip
import com.emanuelgalvao.studies.ui.adapter.OnboardingAdapter

class ViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tips = arrayOf(
            Tip("Bem-vindo(a)", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ", R.drawable.tip_home),
            Tip("Flashcards", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ", R.drawable.tip_cards),
            Tip("Timer de Estudo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ", R.drawable.tip_time)
        )

       binding.viewPager.adapter = OnboardingAdapter(tips)

    }
}