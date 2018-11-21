package pl.polsl.project.catalogex.display

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.PopupMenu

import kotlinx.android.synthetic.main.activity_category_element_list_screen.*
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import kotlinx.android.synthetic.main.content_category_element_list_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.create.CreateCategoryScreen
import pl.polsl.project.catalogex.data.Category


class ShowCategoryListScreen : AppCompatActivity() {

    var listOfCategory : Category? = null

    fun updateView(){
        val listItems = ArrayList<String>(0)

        for (i in listOfCategory!!.list) {
            listItems.add(i.title)
        }

        val adapter =  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems)
        listKategoryScreen.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(listOfCategory == null) listOfCategory = (ShowMainScreen.actualElement as Category)

        supportActionBar!!.title = listOfCategory!!.title

        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateCategoryScreen::class.java)
            ShowMainScreen.actualElement = listOfCategory
            startActivity(intent)
        }

        listKategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            var intent: Intent? = null

            if((listOfCategory!!.list.get(i) as Category).template == null)
            {
                intent = Intent(this, ShowCategoryListScreen::class.java)
            } else {
                intent = Intent(this, ShowElementListScreen::class.java)
            }

            ShowMainScreen.actualElement = listOfCategory!!.list.get(i)
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

    override fun onResume() {
        super.onResume()
        updateView()
    }

    //TODO listy
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                finish()
            }

        }
        return true
    }
}
