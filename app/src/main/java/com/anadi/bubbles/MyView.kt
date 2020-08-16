package com.anadi.bubbles

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView


class MyView : SurfaceView {
    private var world: BubbleWorld = BubbleWorld()

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        world.init(holder)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        world.width = w
        world.height = h
    }
}