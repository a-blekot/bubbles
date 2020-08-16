package com.anadi.bubbles

import android.graphics.Color
import android.view.SurfaceHolder
import kotlinx.coroutines.*

class BubbleWorld {
    private val bubbles = ArrayList<Bubble>()

    var width: Int = 0
    var height: Int = 0
    private lateinit var holder: SurfaceHolder

    fun init(hldr: SurfaceHolder) {
        holder = hldr

        for (i in 0..60)
            bubbles.add(Bubble.create())

        GlobalScope.launch {
            // give canvas a little time to prepare
            delay(500)
            process()
        }

        GlobalScope.launch {
            // give canvas a little time to prepare
            delay(500)
            draw()
        }
    }

    private suspend fun process() {

        withContext(Dispatchers.Default) {

            while (true) {
                delay(30)

                for (b in bubbles) {
                    b.move()
                    b.processEdges(width, height)
                }

                for (i in 0 until bubbles.lastIndex) {
                    for (j in (i + 1)..bubbles.lastIndex)
                        Bubble.processCollision(bubbles[i], bubbles[j])
                }
            }
        }
    }

    private suspend fun draw() {
        withContext(Dispatchers.Main) {
            while (true) {

                delay(30)
                val canvas = holder.lockCanvas(null)

                canvas?.run {
                    drawColor(Color.WHITE)

                    for (b in bubbles)
                        drawCircle(b.cx, b.cy, b.r, b.paint)
                }

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
