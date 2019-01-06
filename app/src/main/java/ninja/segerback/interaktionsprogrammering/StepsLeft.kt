package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class StepsLeft(context: Context, stepNames: List<String>, pager: ViewPager): LinearLayout(context) {
    private val buttons: List<Button>

    init {
        gravity = Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL

        buttons = stepNames.map {
            val button = Button(context)
            button.text = it
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

        highlightStep(0)
    }

    fun highlightStep(i: Int) {
        for (b in buttons) { b.setBackgroundColor(Color.TRANSPARENT) }
        buttons[i].setBackgroundColor(Color.rgb(200, 200, 255))
    }
}

class StepsAdapter(fm: FragmentManager, private val steps: List<() -> Fragment>) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return steps.size
    }

    override fun getItem(position: Int): Fragment {
        return steps[position]()
    }
}
