package ninja.segerback.interaktionsprogrammering

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout

class Laboration3 : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val combobox = Combobox(this)
        val linearLayout = LinearLayout(this)
        linearLayout.addView(combobox)
        setContentView(linearLayout)
    }
}
