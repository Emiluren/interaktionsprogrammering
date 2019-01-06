package ninja.segerback.interaktionsprogrammering

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class Project : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_pager)

        val stepNames = listOf("Welcome", "Password", "End")
        val steps = listOf({HelloFragment()}, {PasswordFragment()}, {EndFragment()})

        val adapter = StepsAdapter(supportFragmentManager, steps)
        val pager: ViewPager = findViewById(R.id.pager)
        pager.adapter = adapter

        val stepsLeft = StepsLeft(this, stepNames, pager)

        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                stepsLeft.highlightStep(position)
            }
        })

        val pageLayout: LinearLayout = findViewById(R.id.page)
        pageLayout.addView(stepsLeft)
    }
}

class HelloFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_container, container, false)
        val frame = v.findViewById<FrameLayout>(R.id.frame)

        val textView = TextView(context)
        textView.text = "Hello and welcome"
        textView.textSize = 30.0f
        frame.addView(textView)

        return v
    }
}

class PasswordFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_container, container, false)
        val frame = v.findViewById<FrameLayout>(R.id.frame)

        val c = context
        if (c != null) {
            val passwordStrengthMeter = PasswordStrengthMeter(c) { password ->
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
            passwordStrengthMeter.setVisualizer(TextPasswordVisualizer(c))

            val passwordBox = EditText(context)
            passwordBox.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordBox.addTextChangedListener(object : TextWatcher {
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

            frame.addView(passwordPage)
        }

        return v
    }
}

class EndFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_container, container, false)
        val frame = v.findViewById<FrameLayout>(R.id.frame)

        val textView = TextView(context)
        textView.text = "Goodbye"
        textView.textSize = 50.0f
        frame.addView(textView)

        return v
    }
}