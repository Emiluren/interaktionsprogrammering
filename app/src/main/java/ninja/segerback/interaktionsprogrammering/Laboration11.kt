package ninja.segerback.interaktionsprogrammering

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar

class Laboration11 : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        val textfield = EditText(this)
        val rating = RatingBar(this)
        val textarea = EditText(this)

        button.setText("Knapp")
        textfield.setText("Textfält")
        rating.numStars = 5
        rating.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textarea.setText("Ett textfält\nsom klarar\nflera rader")
        textarea.setSingleLine(false)

        val layout = LinearLayout(this)
        layout.gravity = Gravity.CENTER_HORIZONTAL
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(button)
        layout.addView(textfield)
        layout.addView(rating)
        layout.addView(textarea)

        setContentView(layout)
    }
}
