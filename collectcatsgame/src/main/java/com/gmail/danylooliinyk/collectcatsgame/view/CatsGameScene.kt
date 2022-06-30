package com.gmail.danylooliinyk.collectcatsgame.view

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.doOnLayout
import com.gmail.danylooliinyk.collectcatsgame.util.Const.IS_DEBUG
import com.gmail.danylooliinyk.collectcatsgame.util.constructCatView
import com.gmail.danylooliinyk.collectcatsgame.util.constructTextView
import com.gmail.danylooliinyk.collectcatsgame.util.randomColor
import com.gmail.danylooliinyk.collectcatsgame.util.randomPoint

// TODO: Move to async initialization so functions are available without checks after init complete
class CatsGameScene(
    private var sceneView: ViewGroup? = null
) {

    // BL
    private val contextScoped: Context
        get() = sceneView?.context ?: throw IllegalStateException("Accessing context out of scope")
    private val catsCount
        get() = sceneViewScoped.children.count { it is CatView }
    private var sceneWidthPx: Int = 0
    private var sceneHeightPx: Int = 0

    // Views
    private val sceneViewScoped
        get() = sceneView ?: throw IllegalStateException("Accessing scene view out of scope")
    private lateinit var tvCatsCollected: TextView

    // Debug
    private lateinit var tvDebugCats: TextView
    private lateinit var tvDebugCatsPositions: TextView
    private lateinit var tvDebugSceneSize: TextView

    init {
        constructViews()
        constructDebugViews()

        sceneViewScoped.doOnLayout {
            sceneWidthPx = sceneViewScoped.width
            sceneHeightPx = sceneViewScoped.height

            spawnOne {
                tvCatsCollected
            }

            if (IS_DEBUG) {
                spawn {
                    listOf(
                        tvDebugCats,
                        tvDebugCatsPositions,
                        tvDebugSceneSize
                    )
                }
            }
        }
    }

    private fun constructDebugViews() {
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
        }
    }

    private fun constructViews() {
        tvCatsCollected = constructTextView(
            context = contextScoped,
            text = "Cats collected: 0"
        )
    }

    fun updateCatsCollected(number: Int) = updateText(tvCatsCollected, "Cats collected: $number")

    fun spawnCatAtRandomPosition() {
        if (catsCount >= MAX_CATS_NUMBER) return
        if (!sceneViewScoped.isLaidOut) return

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

    fun updateSceneSize() = updateText(
        tvDebugSceneSize,
        "Scene size: ${sceneViewScoped.width}:${sceneViewScoped.height}"
    )

    fun updateDebugCats() = updateText(tvDebugCats, "Cats: $catsCount")

    fun updateDebugCatsPositions() {
        val positions = sceneViewScoped
            .children
            .filter { it is CatView }
            .map {
                (it.x to it.y).toString() + "\n"
            }.toList()
        updateText(tvDebugCatsPositions, "Positions: \n${positions}")
    }

    private fun spawn(viewToSpawn: () -> List<View>) {
        viewToSpawn().forEach {
            sceneViewScoped.addView(it)
        }
    }

    private fun spawnOne(viewToSpawn: () -> View) = spawn { listOf(viewToSpawn()) }

    private fun updateText(textView: TextView, text: String) {
        textView.text = text
    }

    companion object {

        private const val MAX_CATS_NUMBER = 7
    }
}
