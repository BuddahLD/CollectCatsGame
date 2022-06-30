package com.gmail.danylooliinyk.collectcirclesgame

import com.gmail.danylooliinyk.collectcirclesgame.Const.IS_DEBUG
import kotlinx.coroutines.CoroutineScope

class CollectCatsGame(
    sceneView: FrameLayoutImageViewSelfDelete,
    sceneWidthPx: Int,
    sceneHeightPx: Int,
    coroutineScope: CoroutineScope
) {

    private val gameScene: CatsGameScene
    private val screenRefresher: ScreenRefresher
    private var catsCollected: Int = 0
    private var catsCombo: Int = 0

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
        sceneView.setOnChildViewRemovedListener {
            catsCollected++
            catsCombo++
        }
    }

    fun start() {
        screenRefresher.start()
    }

    fun stop() {
        screenRefresher.stop()
    }

    private fun onNextFrame() {
        gameScene.spawnCatAtRandomPosition()
        gameScene.updateCatsCollected(catsCollected)
        if (catsCombo > 3) {
            catsCombo = 0
            gameScene.showCatsCombo()
        }
        if (IS_DEBUG) {
            gameScene.updateDebugCats()
            gameScene.updateDebugCatsPositions()
            gameScene.updateSceneSize()
        }
    }

    companion object {

        private const val REFRESH_RATE_60 = 60
    }
}