package com.emanuelgalvao.studies.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.databinding.ActivityResultBinding
import com.emanuelgalvao.studies.model.StudyResult
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class ResultActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityResultBinding

    private lateinit var mResult: StudyResult
    private lateinit var mDeckId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        mResult = intent.extras?.getSerializable("result") as StudyResult
        mDeckId = intent.extras?.getString("deckId").toString()

        binding.textTotal.text = "${mResult.cardsNumber} cartas"
        binding.textCorrects.text = "${mResult.correctCardsNumber} acertos"
        binding.textErrors.text = "${mResult.cardsNumber - mResult.correctCardsNumber} erros"

        listeners()
        loadChart()
    }

    private fun listeners() {
        binding.buttonBackDeck.setOnClickListener(this)
        binding.buttonRestart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_back_deck -> finish()
            R.id.button_restart -> restartStudy()
        }
    }

    private fun restartStudy() {
        val intent = Intent(this, CardsActivity::class.java)
        val bundle = Bundle()
        bundle.putString("deckId", mDeckId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun loadChart() {

        val correctPecent: Float = mResult.correctCardsNumber.toFloat() / mResult.cardsNumber.toFloat()
        val errosrPecent: Float = (1 - correctPecent)

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(correctPecent, "de Acertos"))
        entries.add(PieEntry(errosrPecent, "de Erros"))

        val colors: ArrayList<Int> = ArrayList()

        colors.add(Color.parseColor("#28a745"))
        colors.add(Color.parseColor("#dc3545"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val mChart = binding.chartResult

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(mChart))
        data.setValueTextSize(20f)
        data.setValueTextColor(Color.WHITE)

        mChart.data = data
        mChart.invalidate()

        mChart.legend.isEnabled = false
        mChart.isDrawHoleEnabled = false
        mChart.setUsePercentValues(true)
        mChart.setEntryLabelTextSize(12f)
        mChart.setEntryLabelColor(Color.WHITE)
        mChart.description.isEnabled = false
    }
}