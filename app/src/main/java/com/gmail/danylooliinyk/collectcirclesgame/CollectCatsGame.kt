package com.gmail.danylooliinyk.collectcirclesgame

import kotlinx.coroutines.CoroutineScope

class CollectCatsGame(
    sceneView: FrameLayoutChildClickSelfDelete,
    sceneWidthPx: Int,
    sceneHeightPx: Int,
    coroutineScope: CoroutineScope
) {

    private val gameScene: CatsGameScene
    private val screenRefresher: ScreenRefresher

    init {
        gameScene = CatsGameScene(
            sceneView = sceneView,
            sceneWidthPx = sceneWidthPx,
            sceneHeightPx = sceneHeightPx
        )
        screenRefresher = ScreenRefresher(
            REFRESH_RATE_60,
            coroutineScope,
            ::onNextFrame
        )
    }

    fun start() {
        screenRefresher.start()
    }

    fun stop() {
        screenRefresher.stop()
    }

    private fun onNextFrame() {
        gameScene.spawnCatAtRandomPosition()
    }

    companion object {

        private const val REFRESH_RATE_60 = 60
    }
}