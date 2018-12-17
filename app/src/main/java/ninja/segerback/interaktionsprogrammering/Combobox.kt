package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.*

class Combobox(context: Context) : LinearLayout(context) {
    private val editText = EditText(context)
    private var window: PopupWindow? = null
    private val resultList = PopupResultList(context)

    init {
        editText.width = WindowManager.LayoutParams.FILL_PARENT
        addView(editText)
        addView(resultList)

//        editText.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                Log.d("combo", "tenars")
//                window = popupWindow()
//                window?.showAsDropDown(editText, 0, 0)
//            }
//        })

        // TODO: Add onItemClickListener to listView
    }

    fun popupWindow(): PopupWindow {
        val popupWindow = PopupWindow(context)

        // TODO: Make this from http response
        val list = listOf("Test", "yolo")
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, list)
        //listView.adapter = adapter

        popupWindow.isFocusable = true
        popupWindow.width = 250
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        //popupWindow.contentView = listView
        Log.d("combo pop", "uwlfpuqwlfp")
        return popupWindow
    }
}