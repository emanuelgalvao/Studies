package com.emanuelgalvao.studies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.emanuelgalvao.studies.R
import com.emanuelgalvao.studies.model.Tip

class OnboardingAdapter(private val tips: Array<Tip>): PagerAdapter() {

    override fun getCount(): Int = tips.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.adapter_tip, container, false)

        view.findViewById<TextView>(R.id.text_tip_title).text = tips[position].title
        view.findViewById<TextView>(R.id.text_tip_subtitle).text = tips[position].subtitle
        view.findViewById<ImageView>(R.id.image_tip).setImageResource(tips[position].image)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}