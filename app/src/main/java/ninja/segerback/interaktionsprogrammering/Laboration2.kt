package ninja.segerback.interaktionsprogrammering

import android.os.Bundle
import android.app.Activity
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.ExpandableListView.PACKED_POSITION_VALUE_NULL
import java.util.regex.Pattern

const val NAME = "NAME"

enum class MATCH_TYPE {
    Partial, Invalid, Full
}

class Laboration2 : Activity() {
    private lateinit var browser: ExpandableListView
    private lateinit var adapter: SimpleExpandableListAdapter
    private lateinit var patheditor: EditText

    private val pattern = Pattern.compile("/(?<group>[^/]*)(/(?<child>[^/]*))?")

    private var previosGroup = -1
    private var previosChild = -1

    // Data is expected to be constant, this list is used both for generation
    // of the list view and to search for matches from input
    private val data = listOf(
        Pair("light", listOf("pink", "grey")),
        Pair("medium", listOf("green", "yellow", "red", "blue")),
        Pair("light", listOf("beige")),
        Pair("dark", listOf("black", "purple"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboration2)

        patheditor = findViewById(R.id.pathedit)
        browser = findViewById(R.id.browser)
        adapter = makeAdapter()

        browser.setAdapter(adapter)
        browser.setOnChildClickListener { _, _ , group, child, _ -> onChildClick(group, child) }
        browser.setOnGroupClickListener { _, _ , group, _ -> onGroupClick(group) }
        patheditor.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChanged()
            }
        })
    }

    private fun makeAdapter(): SimpleExpandableListAdapter {
        // Based on example found here https://stackoverflow.com/questions/18350824/

        val groupData = data.map { (groupName, _) -> mapOf(NAME to groupName) }
        val childData = data.map { (_, children) ->
            children.map { child -> mapOf(NAME to  child) }
        }

        return SimpleExpandableListAdapter(this,
            groupData, android.R.layout.simple_expandable_list_item_1,
            arrayOf(NAME), IntArray(1) { android.R.id.text1 },
            childData, android.R.layout.simple_expandable_list_item_2,
            arrayOf(NAME), IntArray(1) { android.R.id.text1 }
        )
    }

    private fun onChildClick(groupPosition: Int, childPosition: Int): Boolean {
        val group = (adapter.getGroup(groupPosition) as Map<String, String>)[NAME]
        val child = (adapter.getChild(groupPosition, childPosition) as Map<String, String>)[NAME]
        patheditor.setText("/$group/$child")

        previosChild = groupPosition
        previosGroup = childPosition

        return true
    }

    private fun onGroupClick(groupPosition: Int): Boolean {
        val group = (adapter.getGroup(groupPosition) as Map<String, String>)[NAME]
        patheditor.setText("/$group")

        previosChild = -1
        previosGroup = groupPosition

        return false
    }

    private fun afterTextChanged() {
        val text = patheditor.text
        val match = pattern.matcher(text)

        var groupIndex = -1
        var childIndex = -1

        val matchType = if (match.matches()) {
            val group = match.group("group")
            val child = match.group("child")

            if (child == null) {
                // The user only wrote a group name
                val found = data.find { (groupName, _) -> groupName == group }
                if (found != null) {
                    groupIndex = data.indexOf(found)
                    MATCH_TYPE.Full
                } else if (data.any { (groupName, _) -> groupName.startsWith(group) }) {
                    MATCH_TYPE.Partial
                } else {
                    MATCH_TYPE.Invalid
                }
            } else {
                // The user typed both a group name and a child name
                var foundExactMatch = false
                groupLoop@ for (i in 0 until data.size) {
                    val (groupName, children) = data[i]
                    if (groupName == group) {
                        for (j in 0 until children.size) {
                            if (children[j] == child) {
                                foundExactMatch = true
                                groupIndex = i
                                childIndex = j
                                break@groupLoop
                            } else if (children[j].startsWith(child)) {
                                groupIndex = i
                                childIndex = j
                            }
                        }
                    }
                }

                when {
                    childIndex == -1 -> MATCH_TYPE.Invalid
                    foundExactMatch -> MATCH_TYPE.Full
                    else -> MATCH_TYPE.Partial
                }
            }
        } else if (text.isEmpty()) {
            // An empty string counts as a partial match
            MATCH_TYPE.Partial
        } else {
            // The user has typed a string which does not start with a /
            MATCH_TYPE.Invalid
        }

        patheditor.setBackgroundColor(Color.WHITE)
        when (matchType) {
            MATCH_TYPE.Full -> {
                if (childIndex == -1) {
                    browser.setSelectedGroup(groupIndex)
                    previosChild = -1
                    previosGroup = groupIndex
                } else {
                    browser.setSelectedChild(groupIndex, childIndex, true)
                    previosChild = childIndex
                    previosGroup = groupIndex
                }
            }
            MATCH_TYPE.Partial -> {
                if (previosGroup != -1) {
                    if (previosChild != -1) {
                        browser.setSelectedChild(previosGroup, previosChild, true)
                    } else {
                        browser.setSelectedGroup(previosGroup)
                    }
                }
            }
            MATCH_TYPE.Invalid -> {
                patheditor.setBackgroundColor(Color.RED)
                //browser.clearChoices()
//                val selectedPos = browser.selectedPosition
//                if (selectedPos != ExpandableListView.PACKED_POSITION_VALUE_NULL) {
//                    browser.setItemChecked(browser.getFlatListPosition(selectedPos), false)
//                }
            }
        }
    }
}
