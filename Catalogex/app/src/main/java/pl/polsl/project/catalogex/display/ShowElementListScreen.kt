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
import pl.polsl.project.catalogex.interfaces.ReturnDialogInterface
import pl.polsl.project.catalogex.create.CreateElementScreen
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.dialogs.SortDialog
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.categoryList.CategoryListViewAdapter

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowElementListScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var listOfElements : Category? = null
    private var displayedList: ArrayList<ListItem> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var searchWindow : SearchView? = null
    private val sortDialog = SortDialog()
    private var isSelectionMode = false
    private var multichoiceDelete = false


    fun updateView(text: String = ""){

        displayedList.clear()
        for(item in listOfElements!!.list){

            if(text.isEmpty()) displayedList.add(item) else
                if(item.title.toUpperCase().contains(text.toUpperCase()))
                    displayedList.add(item)
        }

        sortDialog.sortTable(displayedList)

        val adapter = CategoryListViewAdapter(layoutInflater, displayedList)
        adapter.setIsSelectionMode(isSelectionMode)
        listCategoryScreen.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(listOfElements == null) listOfElements = ShowMainScreen.actualElement as Category

        supportActionBar!!.title = listOfElements!!.title

        listCategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.menu_element_popup, popup.menu)
            menuPopupPosition = listOfElements!!.list.indexOf(displayedList.get(i))
            popup.setOnMenuItemClickListener(this)
            popup.show()
            true
        }

        listCategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = listOfElements!!.list.get(listOfElements!!.list.indexOf(displayedList.get(i)))
            startActivity(intent)
        }

        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateElementScreen::class.java)
            ShowMainScreen.actualElement = listOfElements
            startActivity(intent)
        }

        listCategoryScreen.setMultiChoiceModeListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_element, menu)
        inflater.inflate(R.menu.menu_search, menu)

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
                }
        )
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
                sortDialog.setElement(listOfElements!!.template)
                sortDialog.setActivity(this)
                sortDialog.show(supportFragmentManager, "sort")
            }

            R.id.delete ->{
                multichoiceDelete = true
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.addToDoList ->{
                multichoiceDelete = false
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
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
                deleteElement(elem)
                Toast.makeText(this, getString(R.string.deleted_element) +": " + elem.title,Toast.LENGTH_SHORT) .show()
            }

            R.id.addToDoList -> {
                moveToTODO(elem)
                Toast.makeText(this, getString(R.string.moved) +": " + elem.title, Toast.LENGTH_SHORT) .show()
            }

        }
        updateView()
        return true
    }

    fun moveToTODO(elem:Element){
        listOfElements!!.list.remove(elem)
        ShowMainScreen.todoList.list.add(elem)
        elem.todo = true
    }

    fun deleteElement(element: Element){
        listOfElements!!.list.remove(element)
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
        (listCategoryScreen.adapter as CategoryListViewAdapter).getSelectedList().clear()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        var selected = (listCategoryScreen.adapter as CategoryListViewAdapter).getSelectedList()

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
                    moveToTODO(selected.get(i) as Element)
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
