package com.emanuelgalvao.studies.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.viewpager.widget.ViewPager
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityViewPagerBinding
import com.emanuelgalvao.studies.model.Tip
import com.emanuelgalvao.studies.ui.adapter.OnboardingAdapter

class ViewPagerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tips = arrayOf(
            Tip("Bem-vindo(a)", "Este é o aplicativo que vai te auxiliar em seus estudos diários. Nosso objetivo é usar métodos tradicionais aliados a tecnologia para te ajudar.", R.drawable.tip_home),
            Tip("Flashcards", "Podem ser usados de forma a se reter informações. Eles são perfeitos para aprender vocabulário, fórmulas e leis. Também são excelentes para avaliar os seus conhecimentos sobre um tema.", R.drawable.tip_cards),
            Tip("Timer de Estudo", "Pode ser usado para definir um tempo que você deve ficar focado estudando algum conteúdo. Isso é muito importande tendo em vista que hoje em dia temos muitas distrações ao nosso redor.", R.drawable.tip_time)
        )

        createDots(tips.size, 0)
        binding.viewPager.adapter = OnboardingAdapter(tips)

        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                createDots(tips.size, position)
            }
        })

        listeners()
    }

    private fun createDots(number: Int, position: Int) {
        binding.dots.removeAllViews()
        Array(number) {
            val textView = TextView(baseContext).apply {
                text = getText(R.string.dotted)
                textSize = 35f
                if (position == it) setTextColor(ContextCompat.getColor(baseContext, R.color.white)) else setTextColor(ContextCompat.getColor(baseContext, R.color.dots_color))
            }
            binding.dots.addView(textView)
        }
    }

    private fun listeners() {
        binding.buttonSkip.setOnClickListener(this)
        binding.buttonNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_skip -> {
                startActivity(Intent(this@ViewPagerActivity, LoginActivity::class.java))
                finish()
            }
            R.id.button_next -> {
                if (binding.viewPager.currentItem == binding.viewPager.size) {
                    startActivity(Intent(this@ViewPagerActivity, LoginActivity::class.java))
                    finish()
                } else {
                    binding.viewPager.currentItem = binding.viewPager.currentItem + 1
                }
            }
        }
    }
}