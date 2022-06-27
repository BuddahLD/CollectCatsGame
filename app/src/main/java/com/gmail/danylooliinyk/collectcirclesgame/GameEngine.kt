package com.gmail.danylooliinyk.collectcirclesgame

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class GameEngine {

    private var gameScene: ViewGroup? = null
    private val gameSceneScoped
        get() = gameScene ?: throw IllegalStateException("Accessing game scene out of it's scope")
    private val context: Context
        get() = gameSceneScoped.context
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var tvCollected: TextView

    private lateinit var tvDebugCats: TextView
    private lateinit var tvDebugCatsPositions: TextView
    private lateinit var tvDebugRemoveCats: TextView

    private var collected = 0
    private var gameSceneWidthPx: Int = 0
    private var gameSceneHeightPx: Int = 0
    private val catViews = mutableListOf<View>()
    private val catViewsToRemove = mutableListOf<View>()
    private val colors = mutableListOf(
        Color.valueOf(Color.parseColor("#EB43CC")),
        Color.valueOf(Color.parseColor("#550CF5")),
        Color.valueOf(Color.parseColor("#F9DE56")),
        Color.valueOf(Color.parseColor("#ED702D")),
        Color.valueOf(Color.parseColor("#FFFFFF"))
    )
    private var comboEnabled = false
    private var comboCounter = 0

    fun init(
        gameScene: ViewGroup,
        gameSceneWidth: Int,
        gameSceneHeight: Int,
        coroutineScope: CoroutineScope
    ) {
        this.gameScene = gameScene
        this.gameSceneWidthPx = gameSceneWidth
        this.gameSceneHeightPx = gameSceneHeight
        this.coroutineScope = coroutineScope
    }

    fun startGame() {
        spawnCurrentCollected()
        if (SHOW_DEBUG) {
            spawnDebugCatViews()
            spawnDebugCatViewsToRemove()
            spawnDebugCatViewsPositions()
        }
        tickerFlow(period = 500.milliseconds)
            .onEach {
                if (catViews.size < MAX_CATS_NUMBER) {
                    spawnCatAtRandomPosition()
                }
            }
            .launchIn(coroutineScope)
        tickerFlow(period = 50.milliseconds)
            .onEach {
                if (SHOW_DEBUG) {
                    tvDebugCats.text = "Cats: ${catViews.size}"
                    val positions = catViews.map { "X:${it.x} Y:${it.y}\n" }
                    tvDebugCatsPositions.text = "Positions: \n$positions"
                    tvDebugRemoveCats.text = "Remove: ${catViewsToRemove.size}"
                }
                tvCollected.text = "Cats collected: $collected"
                removeCollectedCatViews()
                if (comboCounter > 3) {

                }
            }
            .launchIn(coroutineScope)
    }

    private fun removeCollectedCatViews() {
        catViewsToRemove.forEach {
            catViews -= it
            gameSceneScoped.removeView(it)
            comboCounter++
        }
        catViewsToRemove.clear()
    }

    private fun spawnCurrentCollected() {
        tvCollected = TextView(context)
        val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        tvCollected.layoutParams = layoutParams
        tvCollected.text = "Collected: $collected"
        tvCollected.textSize = 6.toPx.toFloat()
        tvCollected.setTextColor(TEXT_COLOR)
        tvCollected.y = 16.toPx.toFloat()
        gameSceneScoped.addView(tvCollected)
    }

    private fun spawnDebugCatViews() {
        tvDebugCats = TextView(context)
        val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)
        layoutParams.gravity = Gravity.START
        tvDebugCats.layoutParams = layoutParams
        tvDebugCats.text = "Cats: ${catViews.size}"
        tvDebugCats.textSize = 4.toPx.toFloat()
        tvDebugCats.setTextColor(TEXT_COLOR)
        tvDebugCats.y = 16.toPx.toFloat()
        gameSceneScoped.addView(tvDebugCats)
    }

    private fun spawnDebugCatViewsPositions() {
        tvDebugCatsPositions = TextView(context)
        val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)
        layoutParams.gravity = Gravity.START
        tvDebugCatsPositions.layoutParams = layoutParams
        tvDebugCatsPositions.text = "Positions: ${catViews.size}"
        tvDebugCatsPositions.textSize = 4.toPx.toFloat()
        tvDebugCatsPositions.setTextColor(TEXT_COLOR)
        tvDebugCatsPositions.y = 30.toPx.toFloat()
        gameSceneScoped.addView(tvDebugCatsPositions)
    }

    private fun spawnDebugCatViewsToRemove() {
        tvDebugRemoveCats = TextView(context)
        val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)
        layoutParams.gravity = Gravity.END
        tvDebugRemoveCats.layoutParams = layoutParams
        tvDebugRemoveCats.text = "Remove: ${catViewsToRemove.size}"
        tvDebugRemoveCats.textSize = 4.toPx.toFloat()
        tvDebugRemoveCats.setTextColor(TEXT_COLOR)
        tvDebugRemoveCats.y = 16.toPx.toFloat()
        gameSceneScoped.addView(tvDebugRemoveCats)
    }

    fun stopGame() {
        coroutineScope.cancel()
    }

    fun dipsose() {
        gameScene = null
    }

    private fun spawnCat(point: Point, color: Color) {
        val ivButton = ImageView(context)
        val layoutParams = FrameLayout.LayoutParams(24.toPx, 24.toPx)
        ivButton.layoutParams = layoutParams
        ivButton.setImageResource(R.drawable.ic_cat_24)
        ivButton.x = point.x.toFloat()
        ivButton.y = point.y.toFloat()
        ivButton.setOnClickListener {
            collected++
            catViewsToRemove += it
        }
        ivButton.imageTintMode = PorterDuff.Mode.SRC_IN
        ivButton.imageTintList = ColorStateList.valueOf(color.toArgb())
        catViews += ivButton
        gameSceneScoped.addView(ivButton)
    }

    private fun randomPosition(): Point {
        val positionX = Random.nextInt(gameSceneWidthPx - 24.toPx)
        val positionY = Random.nextInt(gameSceneHeightPx - 24.toPx)
        return Point(positionX, positionY)
    }

    private fun spawnCatAtRandomPosition() {
        val position = randomPosition()
        val color = colors.random()
        spawnCat(position, color)
    }

    companion object {

        private const val MAX_CATS_NUMBER = 7
        private const val TEXT_COLOR = Color.WHITE
        private const val SHOW_DEBUG = true
    }
}
