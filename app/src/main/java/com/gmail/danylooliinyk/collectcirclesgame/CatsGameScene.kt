package com.gmail.danylooliinyk.collectcirclesgame

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.doOnLayout
import com.gmail.danylooliinyk.collectcirclesgame.Const.IS_DEBUG

class CatsGameScene(
    private var sceneView: ViewGroup? = null
) {

    private val tvCatsCollected: TextView

    private val sceneViewScoped
        get() = sceneView ?: throw IllegalStateException("Accessing scene view out of scope")
    private val contextScoped: Context
        get() = sceneView?.context ?: throw IllegalStateException("Accessing context out of scope")
    private val catsCount
        get() = sceneViewScoped.children.count { it is ImageView }
    private var sceneWidthPx: Int = 0
    private var sceneHeightPx: Int = 0

    // Debug
    private lateinit var tvDebugCats: TextView
    private lateinit var tvDebugCatsPositions: TextView
    private lateinit var tvDebugSceneSize: TextView

    init {
        tvCatsCollected = constructTextView(
            context = contextScoped,
            text = "Cats collected: 0"
        )
        spawnOne {
            tvCatsCollected
        }
        sceneViewScoped.doOnLayout {
            sceneWidthPx = sceneViewScoped.width
            sceneHeightPx = sceneViewScoped.height
        }

        if (IS_DEBUG) {
            tvDebugCats = constructTextView(
                context = contextScoped,
                text = "Cats: 0",
                textSize = 8F,
                gravity = Gravity.START
            )
            tvDebugCatsPositions = constructTextView(
                context = contextScoped,
                text = "Positions: []",
                textSize = 8F,
                gravity = Gravity.START,
                position = Point(0, 30)
            )
            tvDebugSceneSize = constructTextView(
                context = contextScoped,
                text = "Scene size: 0:0",
                textSize = 8F,
                gravity = Gravity.END
            )
            spawn {
                listOf(
                    tvDebugCats,
                    tvDebugCatsPositions,
                    tvDebugSceneSize
                )
            }
        }
    }

    fun updateCatsCollected(number: Int) {
        tvCatsCollected.text = "Cats collected: $number"
    }

    fun updateDebugCats() {
        tvDebugCats.text = "Cats: $catsCount"
    }

    fun updateDebugCatsPositions() {
        val positions = sceneViewScoped
            .children
            .filter { it is ImageView }
            .map {
                (it.x to it.y).toString() + "\n"
            }.toList()
        tvDebugCatsPositions.text = "Positions: \n${positions}"
    }

    fun spawnCatAtRandomPosition() {
        if (catsCount >= MAX_CATS_NUMBER) return
        spawnOne {
            constructCatView(
                context = contextScoped,
                point = randomPoint(sceneWidthPx, sceneHeightPx),
                color = randomColor()
            )
        }
    }

    fun showCatsCombo() {
        Toast.makeText(contextScoped, "Cats combo!", Toast.LENGTH_SHORT).show()
    }

    fun updateSceneSize() {
        tvDebugSceneSize.text = "Scene size: ${sceneViewScoped.width}:${sceneViewScoped.height}"
    }

    private fun spawn(viewToSpawn: () -> List<View>) {
        viewToSpawn().forEach {
            sceneViewScoped.addView(it)
        }
    }

    private fun spawnOne(viewToSpawn: () -> View) {
        sceneViewScoped.addView(viewToSpawn())
    }

    companion object {

        private const val MAX_CATS_NUMBER = 7
    }
}
