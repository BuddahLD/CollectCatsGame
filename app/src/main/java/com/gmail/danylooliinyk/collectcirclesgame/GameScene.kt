package com.gmail.danylooliinyk.collectcirclesgame

import android.view.View
import android.view.ViewGroup

interface GameScene {

    fun init(scene: ViewGroup, width: Int, height: Int)

    fun addView(view: View)

    fun removeView(view: View)

    fun clear()
}
