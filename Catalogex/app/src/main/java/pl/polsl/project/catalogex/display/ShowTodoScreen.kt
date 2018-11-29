package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import kotlinx.android.synthetic.main.activity_todo_screen.*
import pl.polsl.project.catalogex.R
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.PopupMenu
import android.widget.Toast
import pl.polsl.project.catalogex.interfaces.ReturnDialogInterface
import pl.polsl.project.catalogex.interfaces.TodoElementInterface
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.dialogs.FilterDialog
import pl.polsl.project.catalogex.dialogs.SortDialog
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.todoElement.TodoElementListViewAdapter
import kotlin.collections.ArrayList

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowTodoScreen : AppCompatActivity(), TodoElementInterface, PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var todoList: Category? = null
    private var displayedList: ArrayList<Element> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var isSelectionMode = false
    private var multichoiceDelete = false

    @Suppress("UNCHECKED_CAST")
    private fun updateView(){

        displayedList.clear()
        for(item in todoList!!.list){
            displayedList.add(item as Element)
        }

        ShowMainScreen.sortDialog.sortTable(displayedList as ArrayList<ListItem>)
        ShowMainScreen.filterDialog.filter(displayedList as ArrayList<ListItem>)

        val adapter = TodoElementListViewAdapter(this, displayedList, layoutInflater, this)
        adapter.setIsSelectionMode(isSelectionMode)
        listTodo.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(todoList == null) todoList = ShowMainScreen.todoList

        listTodo.setMultiChoiceModeListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_todo, menu)
        return true
    }

    override fun setListElement(position: Int) {
        menuPopupPosition = todoList!!.list.indexOf(displayedList.get(position))
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

            R.id.sort ->{
                ShowMainScreen.sortDialog.setActivity(this)
                ShowMainScreen.sortDialog.setElement(Element())
                ShowMainScreen.sortDialog.show(supportFragmentManager, "sort")
            }

            R.id.filtr ->{
                ShowMainScreen.filterDialog.setActivity(this)
                ShowMainScreen.filterDialog.setCategory(ShowMainScreen.todoList)
                ShowMainScreen.filterDialog.show(supportFragmentManager, "filter")
            }

            R.id.delete ->{
                multichoiceDelete = true
                listTodo.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.fromToDo ->{
                multichoiceDelete = false
                listTodo.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {

        var elem = todoList!!.list.get(menuPopupPosition) as Element

        when (item.itemId) {

            R.id.edit -> {
                val intent = Intent(this, EditElementScreen::class.java)
                ShowMainScreen.actualElement = todoList
                intent.putExtra("ELEMENT_NUMBER", todoList!!.list.indexOf(elem))
                startActivity(intent)
            }

            R.id.delete -> {
                deleteElement(elem)
                Toast.makeText(this, getString(R.string.deleted_element) +": " + elem.title,Toast.LENGTH_SHORT) .show()
            }

            R.id.fromToDo -> {
                moveFromTODO(elem)
                Toast.makeText(this, getString(R.string.moved) +": " + elem.title, Toast.LENGTH_SHORT) .show()
            }

        }
        updateView()
        return true
    }

    fun moveFromTODO(elem:Element){
        Utility.deleteElement(elem)
        todoList!!.list.remove(elem)
        elem.todo = false
        Utility.insertElement(elem)
        elem.category!!.list.add(elem)
    }

    fun deleteElement(element: Element){
        todoList!!.list.remove(element)
    }

    override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {}

    override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {

        if(multichoiceDelete) {
            menuInflater.inflate(R.menu.multichoice_menu_delete, p1)

        }else {
            menuInflater.inflate(R.menu.multichoice_menu_accept, p1)
        }

        isSelectionMode = true
        updateView()
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean { return true }

    override fun onDestroyActionMode(p0: ActionMode?) {
        isSelectionMode = false
        updateView()
        (listTodo.adapter as TodoElementListViewAdapter).getSelectedList().clear()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        var selected = (listTodo.adapter as TodoElementListViewAdapter).getSelectedList()

        when(item!!.itemId){

            R.id.action_delete ->{

                Toast.makeText(this, getString(R.string.deleted_element_list) +": " + selected.size.toString(), Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    deleteElement(selected.get(i) as Element)
                }
            }

            R.id.action_accept ->{

                Toast.makeText(this, getString(R.string.moved) +": " + selected.size.toString(), Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    moveFromTODO(selected.get(i) as Element)
                }

            }
        }

        selected.clear()
        mode!!.finish()
        return true
    }

    override fun doReturn() {
        updateView()
    }
}
