package ninja.segerback.interaktionsprogrammering

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.regex.Pattern

enum class MATCH_TYPE {
    Partial, Invalid, Full
}

class Laboration2 : Activity() {
    private lateinit var browser: ExpandableListView
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var patheditor: EditText

    private var inClickManager = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboration2)

        patheditor = findViewById(R.id.pathedit)
        browser = findViewById(R.id.browser)
        adapter = ExpandableListAdapter(this)

        browser.setAdapter(adapter)
        browser.setOnChildClickListener { _, _ , group, child, _ -> onChildClick(group, child) }
        browser.setOnGroupClickListener { _, _ , group, _ -> onGroupClick(group) }
        patheditor.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!inClickManager) {
                    afterTextChanged()
                }
            }
        })
    }

    private fun onChildClick(groupPosition: Int, childPosition: Int): Boolean {
        val (group, children) = (adapter.getGroup(groupPosition) as Pair<String, List<String>>)
        val child = children[childPosition]

        inClickManager = true
        patheditor.setText("/$group/$child")
        inClickManager = false

        adapter.selectChildFromPosition(groupPosition, childPosition)

        return true
    }

    private fun onGroupClick(groupPosition: Int): Boolean {
        val (group, _) = (adapter.getGroup(groupPosition) as Pair<String, List<String>>)

        inClickManager = true
        patheditor.setText("/$group")
        inClickManager = false

        adapter.selectGroupFromPosition(groupPosition)

        return false
    }

    private fun afterTextChanged() {
        patheditor.setBackgroundColor(Color.WHITE)

        val matchResult = adapter.selectFromText(patheditor.text.toString())

        val bg = if (matchResult == MATCH_TYPE.Invalid) {
            Color.RED
        } else {
            Color.WHITE
        }

        if (matchResult == MATCH_TYPE.Full) {
            browser.expandGroup(adapter.selectedGroupPosition)
        }
        patheditor.setBackgroundColor(bg)
    }
}

class ExpandableListAdapter(context: Context): BaseExpandableListAdapter() {
    private val context = context
    private var selectedGroup = ""
    private var selectedChild = ""
    private var showSelection = true
    var selectedGroupPosition = -1

    private val pattern = Pattern.compile("/(?<group>[^/]*)(/(?<child>[^/]*))?")

    val LIGHT_BLUE = Color.rgb(100, 200, 255)

    // Data is expected to be constant, this list is used both for generation
    // of the list view and to search for matches from input
    private val data = listOf(
        Pair("light", listOf("pink", "grey")),
        Pair("medium", listOf("green", "yellow", "red", "blue")),
        Pair("light", listOf("beige")),
        Pair("dark", listOf("black", "black", "purple"))
    )

    fun selectGroupFromPosition(groupPosition: Int) {
        showSelection = true
        val (groupName, _) = data[groupPosition]
        selectedGroup = groupName

        super.notifyDataSetChanged()
    }

    fun selectChildFromPosition(groupPosition: Int, childPosition: Int) {
        showSelection = true
        val (groupName, children) = data[groupPosition]
        selectedGroup = groupName
        selectedChild = children[childPosition]

        super.notifyDataSetChanged()
    }

    fun selectFromText(text: String): MATCH_TYPE {
        val match = pattern.matcher(text)

        val matchResult = if (match.matches()) {
            val group = match.group("group")
            val child = match.group("child")

            if (child == null) {
                // The user only wrote a group name
                val foundExact = data.find { (groupName, _) -> groupName == group }
                val foundPartial = data.any { (groupName, _) -> groupName.startsWith(group) }

                showSelection = true
                selectedGroupPosition = -1
                when {
                    foundExact != null -> {
                        selectedGroup = group
                        selectedChild = ""
                        selectedGroupPosition = data.indexOf(foundExact)
                        MATCH_TYPE.Full
                    }
                    foundPartial -> MATCH_TYPE.Partial
                    else -> {
                        showSelection = false
                        MATCH_TYPE.Invalid
                    }
                }
            } else {
                // The user typed both a group name and a child name
                val foundExact = data.find { (groupName, children) ->
                    groupName == group && children.any { childName -> childName == child }
                }

                val foundPartial = data.any { (groupName, children) ->
                    groupName == group && children.any { childName -> childName.startsWith(child) }
                }

                showSelection = true
                selectedGroupPosition = -1
                when {
                    foundExact != null -> {
                        selectedGroup = group
                        selectedChild = child
                        selectedGroupPosition = data.indexOf(foundExact)
                        MATCH_TYPE.Full
                    }
                    foundPartial -> MATCH_TYPE.Partial
                    else -> {
                        showSelection = false
                        MATCH_TYPE.Invalid
                    }
                }
            }
        } else if (text.isEmpty()) {
            // An empty string counts as a partial match
            showSelection = true
            MATCH_TYPE.Partial
        } else {
            // The user has typed a string which does not start with a /
            showSelection = false
            MATCH_TYPE.Invalid
        }

        super.notifyDataSetChanged()

        return matchResult
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = if (convertView == null) {
            TextView(context)
        } else {
            convertView as TextView
        }

        val (groupName, _) = data[groupPosition]
        view.text = groupName
        view.setPadding(100, 0, 0, 0)
        view.setTextAppearance(context, android.R.style.TextAppearance_Large)

        val bgColor = if (showSelection && selectedGroup == groupName && selectedChild == "") {
            LIGHT_BLUE
        } else {
            Color.TRANSPARENT
        }
        view.setBackgroundColor(bgColor)

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val (_, children) = data[groupPosition]
        return children.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val (_, children) = data[groupPosition]
        return children[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = if (convertView == null) {
            TextView(context)
        } else {
            convertView as TextView
        }

        val (groupName, children) = data[groupPosition]
        view.text = children[childPosition]
        view.setPadding(150, 0, 0, 0)
        view.setTextAppearance(context, android.R.style.TextAppearance_Medium)

        val bgColor = if (showSelection && selectedGroup == groupName && selectedChild == children[childPosition]) {
            LIGHT_BLUE
        } else {
            Color.TRANSPARENT
        }
        view.setBackgroundColor(bgColor)

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition * data.size + childPosition).toLong()
    }

    override fun getGroupCount(): Int {
        return data.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return data[groupPosition]
    }
}