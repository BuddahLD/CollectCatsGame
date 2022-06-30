package com.gmail.danylooliinyk.collectcatsgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gmail.danylooliinyk.collectcatsgame.view.CatViewContainer

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var collectCatsGame: CollectCatsGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flGameScene = findViewById<CatViewContainer>(R.id.flGameScene)
        collectCatsGame = CollectCatsGame(
            sceneView = flGameScene,
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
