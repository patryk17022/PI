package pl.polsl.project.catalogex.display

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_todo_screen.*
import pl.polsl.project.catalogex.R
import java.util.*
import java.util.Arrays.asList
import java.util.Arrays.asList
import android.widget.AdapterView.OnItemLongClickListener
import android.R.id.button1
import android.view.Gravity
import android.widget.*
import pl.polsl.project.catalogex.listElements.CategoryElementListView
import pl.polsl.project.catalogex.listElements.CategoryElementListViewAdapter



class ShowTodoScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_screen)




        //TODO: test - delete

        val listItems = ArrayList<CategoryElementListView>(10)

        for (i in 0 until 10) {
            listItems.add(CategoryElementListView(i,"TODO: "+i))
        }

        val adapter = CategoryElementListViewAdapter(this, listItems,layoutInflater)
        listTodo.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_lists, menu)

        return true
    }
}
