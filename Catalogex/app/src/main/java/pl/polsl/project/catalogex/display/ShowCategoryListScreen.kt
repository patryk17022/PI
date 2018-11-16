package pl.polsl.project.catalogex.display

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.PopupMenu

import kotlinx.android.synthetic.main.activity_category_element_list_screen.*
import kotlinx.android.synthetic.main.content_category_element_list_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.create.CreateCategoryScreen


class ShowCategoryListScreen : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val listItems = ArrayList<String>(10)

        for (i in 0 until 10) {
            listItems.add("Category: " +i)
        }

        val adapter =  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems)
        listKategoryScreen.adapter = adapter


        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateCategoryScreen::class.java)
            startActivity(intent)
        }

        listKategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this, ShowElementListScreen::class.java)
            startActivity(intent)
        }

        listKategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.element_menu_options, popup.menu)
            popup.show()
            true
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_lists, menu)
        inflater.inflate(R.menu.element_menu_search, menu)

        return true
    }
}
