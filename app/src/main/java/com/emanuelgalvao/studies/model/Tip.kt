package com.emanuelgalvao.studies.model

import androidx.annotation.DrawableRes

class Tip {

    var title: String = ""
    var subtitle: String = ""
    @DrawableRes
    var image: Int = 0

    constructor(title: String, subtitle: String, @DrawableRes image: Int) {
        this.title = title
        this.subtitle = subtitle
        this.image = image
    }
}