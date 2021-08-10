package com.emanuelgalvao.studies.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.emanuelgalvao.studies.R
import kotlin.random.Random

class ElementUtils {

    companion object {

        private var selectedBackground: Int = 0

        fun showKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(null, 0)
        }

        fun selectRandomBackground() {
            val backgroundList: List<Int> = listOf(R.drawable.background_1, R.drawable.background_2, R.drawable.background_3, R.drawable.background_4)
            selectedBackground = backgroundList[Random.nextInt(0, backgroundList.size)]
        }

        fun getSelectedBackground():Int {
            return selectedBackground
        }
    }
}