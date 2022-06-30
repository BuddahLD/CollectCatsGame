package com.gmail.danylooliinyk.collectcirclesgame

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

class ScreenRefresher(
    frameRate: Int,
    private val coroutineScope: CoroutineScope,
    onNextFrame: () -> Unit
) {

    private val refreshFlow: Flow<Unit>
    private var job: Job? = null

    init {
        val msPerFrame = ONE_SECOND_MS / frameRate
        refreshFlow = tickerFlow(msPerFrame.milliseconds)
            .onEach { onNextFrame() }
    }

    fun start() {
        job = refreshFlow.launchIn(coroutineScope)
    }

    fun stop() {
        job?.cancel()
    }

    companion object {

        private const val ONE_SECOND_MS = 1000
    }
}
