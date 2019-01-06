package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.LinearLayout

enum class PasswordStrength {
    VeryWeak, Weak, Mediocre, Good, Strong
}

class PasswordStrengthMeter(
    context: Context,
    private val strengthFunction: (String) -> PasswordStrength
) : LinearLayout(context) {
    private var visualzerView: PasswordVisualizer = DefaultPasswordVisualizer(context)

    init {
        addView(visualzerView)
    }

    fun setVisualizer(visualizer: PasswordVisualizer) {
        removeAllViews()
        visualzerView = visualizer
        addView(visualzerView)
    }

    fun showStrengthFor(password: String) {
        visualzerView.setStrength(strengthFunction(password))
        invalidate()
    }
}

abstract class PasswordVisualizer(context: Context): View(context) {
    abstract fun setStrength(strength: PasswordStrength)
}

open class DefaultPasswordVisualizer(context: Context): PasswordVisualizer(context) {
    protected var currentStrength = PasswordStrength.VeryWeak

    protected val redPaint = Paint()
    protected val orangePaint = Paint()
    protected val yellowPaint = Paint()
    protected val lightGreenPaint = Paint()
    protected val greenPaint = Paint()

    protected var barWidth = 400
    protected var barHeight = 50

    init {
        redPaint.color = Color.RED
        orangePaint.color = Color.rgb(255, 100, 100)
        yellowPaint.color = Color.YELLOW
        lightGreenPaint.color = Color.rgb(200, 255, 200)
        greenPaint.color = Color.GREEN
    }

    override fun setStrength(strength: PasswordStrength) {
        currentStrength = strength
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        barWidth = 400
        barHeight = 50
        setMeasuredDimension(barWidth, barHeight)

        Log.d("measure", "$barWidth $barHeight")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("oldpass", "test")

        when (currentStrength) {
            PasswordStrength.VeryWeak -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5, barHeight.toFloat(), redPaint)
            PasswordStrength.Weak -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 2, barHeight.toFloat(), orangePaint)
            PasswordStrength.Mediocre -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 3, barHeight.toFloat(), yellowPaint)
            PasswordStrength.Good -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 4, barHeight.toFloat(), lightGreenPaint)
            PasswordStrength.Strong -> canvas.drawRect(0f, 0f, barWidth.toFloat(), barHeight.toFloat(), greenPaint)
        }
    }
}

class TextPasswordVisualizer(context: Context): DefaultPasswordVisualizer(context) {
    private val textPaint = Paint()
    init {
        textPaint.textSize = 80f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(barWidth, 200)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val strengthStr = when (currentStrength) {
            PasswordStrength.VeryWeak -> "Very weak"
            else -> currentStrength.toString()
        }
        canvas.drawText(strengthStr, 0f, height.toFloat(), textPaint)
    }
}