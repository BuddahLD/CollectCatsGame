package com.gmail.danylooliinyk.collectcirclesgame

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var gameEngine: GameEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameEngine = GameEngine()
        gameEngine?.run {
            val flGameScene = findViewById<ViewGroup>(R.id.flGameScene)
            val display = windowManager.defaultDisplay
            init(
                gameScene = flGameScene,
                gameSceneWidth = display.width,
                gameSceneHeight = display.height,
                coroutineScope = lifecycleScope
            )
            startGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameEngine?.dipsose()
        gameEngine = null
    }
}