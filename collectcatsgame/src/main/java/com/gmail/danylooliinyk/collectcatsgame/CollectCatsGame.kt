package com.gmail.danylooliinyk.collectcatsgame

import com.gmail.danylooliinyk.collectcatsgame.util.Const.IS_DEBUG
import com.gmail.danylooliinyk.collectcatsgame.util.ScreenRefresher
import com.gmail.danylooliinyk.collectcatsgame.view.CatViewContainer
import com.gmail.danylooliinyk.collectcatsgame.view.CatsGameScene
import kotlinx.coroutines.CoroutineScope

class CollectCatsGame(
    sceneView: CatViewContainer,
    coroutineScope: CoroutineScope
) {

    private val gameScene: CatsGameScene
    private val screenRefresher: ScreenRefresher
    private var catsCollected: Int = 0
    private var catsCombo: Int = 0

    init {
        gameScene = CatsGameScene(sceneView = sceneView)
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