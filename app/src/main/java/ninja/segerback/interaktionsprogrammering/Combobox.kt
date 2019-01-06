package ninja.segerback.interaktionsprogrammering

import android.content.Context
import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class Combobox(context: Context) : LinearLayout(context) {
    private val editText = EditText(context)
    private var data = emptyArray<String>()
    private var nextRequestId = 0
    private var previousFetchId = -1

    init {
        editText.width = 600
        orientation = LinearLayout.VERTICAL
        addView(editText)

        val listPopupWindow = ListPopupWindow(context)
        listPopupWindow.anchorView = editText

        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            editText.setText(data[position])
        }

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (editText.text.isEmpty()) {
                    return
                }

                val url = URL("http://andla.pythonanywhere.com/getnames/$nextRequestId/${editText.text}")
                nextRequestId += 1

                DownloadTask { result ->
                    val json = JSONObject(result)
                    val idString = json.getString("id")
                    val id = Integer.parseInt(idString)

                    if (id > previousFetchId) {
                        previousFetchId = id

                        val array = json.getJSONArray("result")
                        val len = Math.min(array.length(), 10)
                        val newData = Array(len) {""}
                        for (i in 0 until len) {
                            newData[i] = array[i] as String
                        }
                        data = newData

                        listPopupWindow.setAdapter(
                            ArrayAdapter(context, R.layout.list_item, data)
                        )
                        listPopupWindow.show()
                    }
                }.execute(url)
            }
        })
    }
}

class DownloadTask(val callback: (String) -> Unit) : AsyncTask<URL, Void, String>() {
    override fun doInBackground(vararg params: URL): String {
        val url = params[0]
        val connection = url.openConnection() as HttpURLConnection
        var result = ""
        try {
            val inputStream = connection.getInputStream()
            val s = Scanner(inputStream).useDelimiter("\\A")
            result = if (s.hasNext()) s.next() else ""
        } finally {
            connection.disconnect()
        }
        return result
    }

    override fun onPostExecute(result: String) {
        callback(result)
    }
}