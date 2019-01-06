package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView

class PopupResultList(context: Context, attributeSet: AttributeSet): TextView(context, attributeSet) {
    private val textPaint = Paint()

    init {
        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.BLACK
        textPaint.textSize = 100f

        Log.d("popup", "hej")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(100, 100)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.textSize = textSize
        canvas.drawText(text.toString(), 0f, 50f, textPaint)
    }
}