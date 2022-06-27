package com.gmail.danylooliinyk.collectcirclesgame

interface LeGameEngine {

    fun init(frameRate: Int = DEFAULT_FRAME_RATE)

    fun onNextFrame(render: () -> Unit)

    companion object {

        private const val DEFAULT_FRAME_RATE = 60
    }
}
