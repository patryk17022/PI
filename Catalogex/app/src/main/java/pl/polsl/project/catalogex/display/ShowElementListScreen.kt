package pl.polsl.project.catalogex.display

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_category_element_list_screen.*
import kotlinx.android.synthetic.main.content_category_element_list_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.`interface`.ReturnDialogInterface
import pl.polsl.project.catalogex.create.CreateElementScreen
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.dialogs.SortDialog
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.CategoryList.CategoryListView


class ShowElementListScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    var listOfElements : Category? = null
    var displayedList: ArrayList<ListItem> = ArrayList()
    var menuPopupPosition: Int = -1
    var searchWindow : SearchView? = null
    val sortDialog = SortDialog()


    fun updateView(text: String = ""){

        displayedList.clear()
        for(item in listOfElements!!.list){

            if(text.isEmpty()) displayedList.add(item) else
                if(item.title.toUpperCase().contains(text.toUpperCase()))
                    displayedList.add(item)
        }

        sortDialog.sortTable(displayedList)

        val adapter = CategoryListView(layoutInflater, displayedList)
        listKategoryScreen.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(listOfElements == null) listOfElements = ShowMainScreen.actualElement as Category

        supportActionBar!!.title = listOfElements!!.title

        listKategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.element_menu_element_popup, popup.menu)
            menuPopupPosition = listOfElements!!.list.indexOf(displayedList.get(i))
            popup.setOnMenuItemClickListener(this)
            popup.show()
            true
        }

        listKategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = listOfElements!!.list.get(listOfElements!!.list.indexOf(displayedList.get(i)))
            startActivity(intent)
        }

        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateElementScreen::class.java)
            ShowMainScreen.actualElement = listOfElements
            startActivity(intent)
        }

        listKategoryScreen.setMultiChoiceModeListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_lists, menu)
        inflater.inflate(R.menu.element_menu_search, menu)

        searchWindow = menu.findItem(R.id.action_search).actionView as SearchView
        searchWindow!!.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(enteredText: String): Boolean {
                        updateView(enteredText)
                        return true
                    }

                })

        return true
    }

    override fun onResume() {
        super.onResume()
        updateView()

        closeSearchWindow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                if (searchWindow!!.isIconified()) {
                    finish()
                }
            }

            R.id.sort ->{
                sortDialog.rating = true
                sortDialog.activity = this
                sortDialog.show(supportFragmentManager, "sort")
            }

            R.id.delete ->{
                listKategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

        }

        closeSearchWindow()

        return super.onOptionsItemSelected(item)
    }

    fun closeSearchWindow(){
        if(searchWindow != null) {
            searchWindow!!.setIconified(true)
            searchWindow!!.onActionViewCollapsed()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        var elem = listOfElements!!.list.get(menuPopupPosition) as Element
        when (item.itemId) {

            R.id.edit -> {
                val intent = Intent(this, EditElementScreen::class.java)
                ShowMainScreen.actualElement = listOfElements
                intent.putExtra("ELEMENT_NUMBER", listOfElements!!.list.indexOf(elem))
                startActivity(intent)
            }

            R.id.delete -> {
                Toast.makeText(this, getString(R.string.deleted_element) +": " + elem.title,Toast.LENGTH_LONG) .show()
                deleteElement(elem)
            }

            R.id.addToDoList -> {
                listOfElements!!.list.remove(elem)
                ShowMainScreen.todoList.list.add(elem)
                elem.todo = true
            }

        }
        updateView()
        return true
    }

    fun deleteElement(element: Element){
        listOfElements!!.list.remove(element)
    }

    override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {

    }

    override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        val inflater = menuInflater
        menuInflater.inflate(R.menu.multichoice_menu,p1)
        ShowCategoryListScreen.isSelectionMode = true
        updateView()
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        ShowCategoryListScreen.isSelectionMode = false
        updateView()
        (listKategoryScreen.adapter as CategoryListView).selectedList.clear()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_delete ->{
                var selected = (listKategoryScreen.adapter as CategoryListView).selectedList
                Toast.makeText(this, getString(R.string.deleted_element_list) +": " + selected.size.toString(), Toast.LENGTH_LONG) .show()

                for(i in 0 until selected.size){
                    deleteElement(selected.get(i) as Element)
                }

                selected.clear()
                mode!!.finish()
            }
        }

        return true
    }

    override fun doReturn() {
        updateView()
    }
}
