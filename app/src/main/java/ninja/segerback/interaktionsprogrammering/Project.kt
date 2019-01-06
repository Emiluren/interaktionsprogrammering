package ninja.segerback.interaktionsprogrammering

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.widget.LinearLayout

class Project : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_pager)

        val adapter = StepsAdapter(supportFragmentManager)
        val pager: ViewPager = findViewById(R.id.pager)
        pager.adapter = adapter

        val pageLayout: LinearLayout = findViewById(R.id.page)
        pageLayout.addView(StepsLeft(this, listOf("Welcome", "Password", "End"), pager))
    }
}