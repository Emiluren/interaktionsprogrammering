package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

const val NUM_STEPS = 2

class StepsLeft(context: Context, stepNames: List<String>, pager: ViewPager): LinearLayout(context) {
    private val buttons: List<Button>

    init {
        gravity = Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL

        buttons = stepNames.mapIndexed { i, step ->
            val step = stepNames[i]
            val button = Button(context)
            button.text = step
            button.setBackgroundColor(Color.TRANSPARENT)
            addView(button)
            button
        }

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                pager.currentItem = i
                highlightStep(i)
            }
        }
    }

    public fun highlightStep(i: Int) {
        for (b in buttons) { b.setBackgroundColor(Color.TRANSPARENT) }
        buttons[i].setBackgroundColor(Color.rgb(200, 200, 255))
    }
}

class StepsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        val frag = StepFragment()

        val args = Bundle()
        args.putInt("num", position)
        frag.arguments = args

        return frag
    }
}

class StepFragment : Fragment() {
    private var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        num = arguments?.getInt("num") ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_container, container, false)
        val frame = v.findViewById<FrameLayout>(R.id.frame)

        val c = context
        when (num) {
            0 -> {
                val textView = TextView(context)
                textView.text = "Hello and welcome"
                textView.textSize = 30.0f
                frame.addView(textView)
            }
            1 -> if (c != null) {
                frame.addView(makePasswordPage(c))
            }
            2 -> {
                val textView = TextView(context)
                textView.text = "Goodbye"
                textView.textSize = 50.0f
                frame.addView(textView)
            }
        }
        return v
    }
}

fun makePasswordPage(context: Context): LinearLayout {
    val passwordStrengthMeter = PasswordStrengthMeter(context) { password ->
        var score = 0

        // Check password length
        if (password.length > 5) {
            score++
        }
        if (password.length > 8) {
            score++
        }

        // Check that the password contains both upper and lower case letters
        if (password.toLowerCase() != password && password.toUpperCase() != password) {
            score++
        }

        // Check that the password contains special characters
        if (password.any { c -> !c.isLetter() }) {
            score++
        }

        when (score) {
            0 -> PasswordStrength.VeryWeak
            1 -> PasswordStrength.Weak
            2 -> PasswordStrength.Mediocre
            3 -> PasswordStrength.Good
            else -> PasswordStrength.Strong
        }
    }
    passwordStrengthMeter.setVisualizer(TextPasswordVisualizer(context))

    val passwordBox = EditText(context)
    passwordBox.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    passwordBox.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable) {
            passwordStrengthMeter.showStrengthFor(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
    })

    val passwordPage = LinearLayout(context)
    passwordPage.orientation = LinearLayout.VERTICAL
    passwordPage.addView(passwordBox)
    passwordPage.addView(passwordStrengthMeter)

    return passwordPage
}