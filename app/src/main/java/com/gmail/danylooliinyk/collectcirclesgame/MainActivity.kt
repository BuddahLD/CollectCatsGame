package com.gmail.danylooliinyk.collectcirclesgame

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var collectCatsGame: CollectCatsGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flGameScene = findViewById<FrameLayoutChildClickSelfDelete>(R.id.flGameScene)
        val display = windowManager.defaultDisplay
        collectCatsGame = CollectCatsGame(
            sceneView = flGameScene,
            sceneWidthPx = display.width,
            sceneHeightPx = display.height,
            coroutineScope = lifecycleScope
        )
        collectCatsGame?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        collectCatsGame?.stop()
        collectCatsGame = null
    }
}
