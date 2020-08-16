package com.anadi.bubbles

import android.graphics.Color
import android.graphics.Paint
import kotlin.math.*
import kotlin.random.Random

const val RADIUS = 30.0F
const val V = 10.0F

typealias FPair = Pair<Float, Float>

class Bubble(
    var vx: Float = V,
    var vy: Float = V,
    var cx: Float = 0.0F,
    var cy: Float = 0.0F,
    val r: Float = RADIUS) {

    val m = r * r
    val paint: Paint = Paint().apply { color = randColor }

    fun move() {
        cx += vx
        cy += vy
    }

    companion object {
//        private val vectorColor = Color.argb(255, 255, 0, 0)
//        val vectorPaint = Paint().apply {
//            color = Bubble.vectorColor
//            textSize = 40f
//        }

        private val rand = Random(System.currentTimeMillis())

        private val randColor: Int
            get() = Color.argb(
                255,
                rand.nextInt(255),
                rand.nextInt(255),
                rand.nextInt(255)
            )

        fun create(): Bubble = Bubble(
            rand.nextInt(10).toFloat(),
            rand.nextInt(10).toFloat(),
            rand.nextInt(500).toFloat(),
            rand.nextInt(500).toFloat(),
            RADIUS
        )

        fun processCollision(b1: Bubble, b2: Bubble) {
            if (!b1.collides(b2))
                return

            fixPosition(b1, b2)

            // fix to assure that value is from -1 to 1
            val value = ((b2.cy - b1.cy) / (b2.r + b1.r)).coerceIn(-1f, 1f)
            val a: Float = asin(value)

            val vx1 = b1.vx * cos(a) - b1.vy * sin(a)
            val vy1 = b1.vy * cos(a) + b1.vx * sin(a)

            val vx2 = b2.vx * cos(a) - b2.vy * sin(a)
            val vy2 = b2.vy * cos(a) + b2.vx * sin(a)

            val vx1n = (vx1 * (b1.m - b2.m) + vx2 * 2 * b2.m) / (b1.m + b2.m)
            val vx2n = (vx2 * (b2.m - b1.m) + vx1 * 2 * b1.m) / (b1.m + b2.m)

            b1.vx = vx1n * cos(-a) - vy1 * sin(-a)
            b1.vy = vy1 * cos(-a) + vx1n * sin(-a)

            b2.vx = vx2n * cos(-a) - vy2 * sin(-a)
            b2.vy = vy2 * cos(-a) + vx2n * sin(-a)
        }
    }
}

fun Bubble.collides(b2: Bubble): Boolean {
    return (r + b2.r) >= distance(b2)
}

fun Bubble.distance(b: Bubble): Float =
    sqrt((cx - b.cx).pow(2) + (cy - b.cy).pow(2))

fun Bubble.processEdges(w: Int, h: Int) {

    if (cx - r < 0) {
        cx = r
        vx = -vx
    } else if (cx + r > w) {
        cx = w - r
        vx = -vx
    } else if (cy - r < 0) {
        cy = r
        vy = -vy
    } else if (cy + r > h) {
        cy = h - r
        vy = -vy
    }
}

/**
 * Little hack. To avoid bubble intersection
 */
fun fixPosition(b1: Bubble, b2: Bubble) {
    if (b1.cx < b2.cx) {
        b1.cx -= 2.0f
        b2.cx += 2.0f
    }
    else if (b1.cx > b2.cx) {
        b1.cx += 2.0f
        b2.cx -= 2.0f
    }

    if (b1.cy < b2.cy) {
        b1.cy -= 2.0f
        b2.cy += 2.0f
    } else if (b1.cy > b2.cy) {
        b1.cy += 2.0f
        b2.cy -= 2.0f
    }
}