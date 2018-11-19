package pl.polsl.project.catalogex.display

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListView
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter


//TODO: schowac przyciski do edycji i usowania strzalka do powrotu
class ShowElementInformationScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        editNameElement.visibility = View.INVISIBLE
        addFeatureButton.visibility = View.INVISIBLE


        val listItems = ArrayList<ElementDetailListView>()

        for (i in 0 until 10) {
            listItems!!.add(ElementDetailListView(i,"title" + i,"123123"))
        }

        val adapter =  ElementDetailListViewAdapter(this,listItems!!,layoutInflater,this, DetailListMode.NONE_BUTTON)
        featureList.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_options, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {


            }

        }
        return true
    }
}


