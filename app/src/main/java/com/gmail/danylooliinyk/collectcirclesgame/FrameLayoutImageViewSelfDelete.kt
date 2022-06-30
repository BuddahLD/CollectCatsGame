package com.gmail.danylooliinyk.collectcirclesgame

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

class FrameLayoutImageViewSelfDelete : FrameLayout, View.OnClickListener {

    private var listener: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addView(child: View?) {
        super.addView(child)
        if (child is ImageView) {
            child.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        removeView(v)
        listener?.invoke()
    }

    fun setOnChildViewRemovedListener(listener: () -> Unit) {
        this.listener = listener
    }
}
