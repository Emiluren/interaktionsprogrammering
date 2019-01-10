package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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

    protected var veryWeakPaint = Paint()
    protected var weakPaint = Paint()
    protected var mediocrePaint = Paint()
    protected var goodPaint = Paint()
    protected var strongPaint = Paint()

    protected var barWidth = 400
    protected var barHeight = 50

    init {
        veryWeakPaint.color = Color.RED
        weakPaint.color = Color.rgb(255, 100, 100)
        mediocrePaint.color = Color.YELLOW
        goodPaint.color = Color.rgb(200, 255, 200)
        strongPaint.color = Color.GREEN
    }

    override fun setStrength(strength: PasswordStrength) {
        currentStrength = strength
        invalidate()
    }

    fun setColors(veryWeak: Paint, weak: Paint, mediocre: Paint, good: Paint, strong: Paint) {
        veryWeakPaint = veryWeak
        weakPaint = weak
        mediocrePaint = mediocre
        goodPaint = good
        strongPaint = strong
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
            PasswordStrength.VeryWeak -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5, barHeight.toFloat(), veryWeakPaint)
            PasswordStrength.Weak -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 2, barHeight.toFloat(), weakPaint)
            PasswordStrength.Mediocre -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 3, barHeight.toFloat(), mediocrePaint)
            PasswordStrength.Good -> canvas.drawRect(0f, 0f, barWidth.toFloat() / 5 * 4, barHeight.toFloat(), goodPaint)
            PasswordStrength.Strong -> canvas.drawRect(0f, 0f, barWidth.toFloat(), barHeight.toFloat(), strongPaint)
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