package pl.polsl.project.catalogex

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView
import kotlinx.android.synthetic.*


class CreateTemplateScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)



      /*  val linearLayout = findViewById<View>(R.id.elemntInfoView) as LinearLayout

        for (i in 1..100){
            val valueTV = TextView(this)
            valueTV.text = "hallo hallo"

            valueTV.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            linearLayout.addView(valueTV)
        }*/

    }
}

