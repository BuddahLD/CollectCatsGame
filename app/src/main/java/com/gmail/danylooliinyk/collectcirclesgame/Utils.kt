package com.gmail.danylooliinyk.collectcirclesgame

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Duration

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

fun Activity.toast(text: String) =
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

fun constructTextView(
    context: Context,
    text: String,
    color: Int = Color.WHITE,
    textSize: Float = 14F,
    position: Point = Point(0, 16),
    gravity: Int = Gravity.CENTER_HORIZONTAL
): TextView {
    val tvCollected = TextView(context)
    val layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    )
    //    layoutParams.setMargins(0, 0, 0, 0)
    layoutParams.gravity = gravity
    tvCollected.layoutParams = layoutParams
    tvCollected.text = text
    tvCollected.textSize = textSize
    tvCollected.setTextColor(color)
    tvCollected.x = position.x.toPx.toFloat()
    tvCollected.y = position.y.toPx.toFloat()
    return tvCollected
}

fun constructCatView(
    context: Context,
    point: Point,
    color: Color
): ImageView {
    val ivButton = ImageView(context)
    val layoutParams = FrameLayout.LayoutParams(24.toPx, 24.toPx)
    ivButton.layoutParams = layoutParams
    ivButton.setImageResource(R.drawable.ic_cat_24)
    ivButton.x = point.x.toFloat()
    ivButton.y = point.y.toFloat()
    ivButton.imageTintMode = PorterDuff.Mode.SRC_IN
    ivButton.imageTintList = ColorStateList.valueOf(color.toArgb())
    return ivButton
}

fun randomPoint(widthBound: Int, heightBound: Int, margins: Int = 24.toPx): Point {
    val positionX = Random.nextInt(widthBound - margins)
    val positionY = Random.nextInt(heightBound - margins)
    return Point(positionX, positionY)
}

fun randomColor(): Color {
    return mutableListOf(
        Color.valueOf(Color.parseColor("#EB43CC")),
        Color.valueOf(Color.parseColor("#550CF5")),
        Color.valueOf(Color.parseColor("#F9DE56")),
        Color.valueOf(Color.parseColor("#ED702D")),
        Color.valueOf(Color.parseColor("#FFFFFF"))
    ).random()
}
