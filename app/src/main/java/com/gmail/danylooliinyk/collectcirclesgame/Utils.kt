package com.gmail.danylooliinyk.collectcirclesgame

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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
