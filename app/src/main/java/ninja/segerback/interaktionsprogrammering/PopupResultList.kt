package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class PopupResultList(context: Context): View(context) {
    val paint = Paint()

    init {
        paint.style = Paint.Style.FILL
        paint.textSize = 100f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawText("hej", 20f, 100f, paint)
    }
}