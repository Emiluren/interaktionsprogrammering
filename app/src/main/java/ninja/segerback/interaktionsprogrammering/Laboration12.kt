package ninja.segerback.interaktionsprogrammering;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.ScriptGroup
import android.text.InputType
import android.widget.*
import android.widget.TableLayout

class Laboration12 : Activity() {
    fun labeledTextField(label: String, content: String, inputType: Int = InputType.TYPE_CLASS_TEXT): TableRow {
        val row = TableRow(this)

        val labelView = TextView(this)
        labelView.text = label
        row.addView(labelView)

        val field = EditText(this)
        field.setText(content)
        field.inputType = inputType
        row.addView(field)

        return row
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = TableLayout(this)
        layout.addView(labeledTextField("Namn", "Emil"))
        val password = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(labeledTextField("Lösenord", "arstsra", password))
        layout.addView(labeledTextField("Epost", "test@example.com", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS))

        val age = TextView(this)
        age.text = "Ålder"
        val ageSlider = SeekBar(this)

        val ageRow = TableRow(this)
        ageRow.orientation = LinearLayout.HORIZONTAL
        ageRow.addView(age)
        ageRow.addView(ageSlider)
        layout.addView(ageRow)

        setContentView(layout)
    }
}
