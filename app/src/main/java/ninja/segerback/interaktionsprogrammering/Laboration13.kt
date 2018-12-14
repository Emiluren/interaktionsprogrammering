package ninja.segerback.interaktionsprogrammering

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.*

class Laboration13 : Activity() {
    fun checkBoxSet(boxlabels: Array<String>, selectedIndex: Int): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL

        for (i in boxlabels.indices) {
            val box = CheckBox(this)

            if (i == selectedIndex) {
                box.isChecked = true
            }

            box.setText(boxlabels[i])
            layout.addView(box)
        }

        return layout
    }

    fun label(text: String): TextView {
        val view = TextView(this)
        view.text = text
        view.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER_HORIZONTAL

        layout.addView(label("Hur trivs du på LiU?InputType.TYPE_CLASS_TEXT |"))
        layout.addView(checkBoxSet(arrayOf("Bra", "Mycket bra", "Jättebra"), 2))

        layout.addView(label("Läser du på LiTH?"))
        layout.addView(checkBoxSet(arrayOf("Ja", "Nej"), 1))

        val img = ImageView(this)
        img.setImageResource(R.drawable.logo)
        layout.addView(img)
        layout.addView(label("Är detta LiUs logotyp"))
        layout.addView(checkBoxSet(arrayOf("Ja", "Nej"), 0))

        val button = Button(this)
        button.setText("Skicka in")
        layout.addView(button)

        setContentView(layout)
    }
}