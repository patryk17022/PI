package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_todo_screen.*
import pl.polsl.project.catalogex.R
import android.view.MenuItem
import android.widget.PopupMenu
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.CategoryElementInterface
import pl.polsl.project.catalogex.listElements.CategoryElementListViewAdapter
import kotlin.collections.ArrayList


class ShowTodoScreen : AppCompatActivity(), CategoryElementInterface, PopupMenu.OnMenuItemClickListener {

    var todoList: Category? = null
    var menuPopupPosition: Int = -1

    fun updateView(){
        val adapter = CategoryElementListViewAdapter(this, todoList!!.list as ArrayList<Element>,layoutInflater,this)
        listTodo.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(todoList == null) todoList = ShowMainScreen.todoList

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_lists, menu)
        return true
    }

    override fun setListElement(postion: Int) {
        menuPopupPosition = postion
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                finish()
            }

        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        var elem = todoList!!.list.get(menuPopupPosition) as Element
        when (item.itemId) {

            R.id.edit -> {
                val intent = Intent(this, EditElementScreen::class.java)
                ShowMainScreen.actualElement = elem
                startActivity(intent)
            }

            R.id.delete -> {
                todoList!!.list.remove(elem)
            }

            R.id.addToDoList -> {
                todoList!!.list.remove(elem)
                elem.category!!.list.add(elem)
                elem.todo = false
            }

        }
        updateView()
        return true
    }

}
