package com.gmail.danylooliinyk.collectcirclesgame

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gmail.danylooliinyk.collectcirclesgame.Const.IS_DEBUG

class CatsGameScene(
    private var sceneView: ViewGroup? = null,
    private var sceneWidthPx: Int = 0,
    private var sceneHeightPx: Int = 0
) {

    private val tvCatsCollected: TextView

    private val sceneViewScoped
        get() = sceneView ?: throw IllegalStateException("Accessing scene view out of scope")
    private val contextScoped: Context
        get() = sceneView?.context ?: throw IllegalStateException("Accessing context out of scope")

    // Debug
    private lateinit var tvDebugCats: TextView
    private lateinit var tvDebugCatsPositions: TextView
    private lateinit var tvDebugRemoveCats: TextView

    init {
        tvCatsCollected = constructTextView(
            context = contextScoped,
            text = "Cats collected: 0"
        )
        spawnOne {
            tvCatsCollected
        }

        if (IS_DEBUG) {
            tvDebugCats = constructTextView(
                context = contextScoped,
                text = "Cats: 0",
                textSize = 4F,
                gravity = Gravity.START
            )
            tvDebugCatsPositions = constructTextView(
                context = contextScoped,
                text = "Positions: []",
                textSize = 4F,
                gravity = Gravity.START,
                position = Point(0, 30)
            )
            tvDebugRemoveCats = constructTextView(
                context = contextScoped,
                text = "Remove: 0",
                textSize = 4F,
                gravity = Gravity.END
            )
            spawn {
                listOf(
                    tvDebugCats,
                    tvDebugCatsPositions,
                    tvDebugRemoveCats
                )
            }
        }
    }

    fun updateCatsCollected(number: Int) {
        tvCatsCollected.text = "Cats collected: $number"
    }

    fun updateDebugCats(number: Int) {
        tvDebugCats.text = "Cats: $number"
    }

    fun updateDebugCatsPositions(positions: List<String>) {
        tvDebugCatsPositions.text = "Positions: $positions"
    }

    fun updateDebugRemoveCats(number: Int) {
        tvDebugRemoveCats.text = "Remove: $number"
    }

    fun spawnCatAtRandomPosition() {
        if (sceneViewScoped.childCount >= MAX_CATS_NUMBER) return

        spawnOne {
            constructCatView(
                context = contextScoped,
                point = randomPoint(sceneWidthPx, sceneHeightPx),
                color = randomColor()
            )
        }
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
