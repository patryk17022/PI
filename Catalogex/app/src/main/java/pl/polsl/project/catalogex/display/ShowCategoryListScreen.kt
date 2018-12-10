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
import pl.polsl.project.catalogex.create.CreateCategoryScreen
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.edit.EditCategoryScreen
import pl.polsl.project.catalogex.listElements.categoryList.CategoryListViewAdapter


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowCategoryListScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var listOfCategory : Category? = null
    private var displayedList: ArrayList<ListItem> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var searchWindow : SearchView? = null
    private var isSelectionMode = false

    fun updateView(text: String = ""){

        displayedList.clear()
        for(item in listOfCategory!!.list){

            if(text.isEmpty())
                displayedList.add(item)
            else
                if(item.title.toUpperCase().contains(text.toUpperCase()))
                    displayedList.add(item)
        }

        ShowMainScreen.sortDialog.sortTable(displayedList)
        ShowMainScreen.filterDialog.filter(displayedList)

        val adapter = CategoryListViewAdapter(layoutInflater, displayedList)
        adapter.setIsSelectionMode(isSelectionMode)
        listCategoryScreen.adapter = adapter

        supportActionBar!!.title = listOfCategory!!.title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(listOfCategory == null) listOfCategory = (ShowMainScreen.actualElement as Category)

        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateCategoryScreen::class.java)
            ShowMainScreen.actualElement = listOfCategory
            startActivity(intent)
        }

        listCategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent: Intent?

            val elem = listOfCategory!!.list[listOfCategory!!.list.indexOf(displayedList[i])]

            intent = if((elem as Category).template == null) {
                Intent(this, ShowCategoryListScreen::class.java)
            } else {
                Intent(this, ShowElementListScreen::class.java)
            }

            ShowMainScreen.actualElement = elem
            startActivity(intent)
        }

        listCategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.menu_options, popup.menu)
            menuPopupPosition = listOfCategory!!.list.indexOf(displayedList[i])
            popup.setOnMenuItemClickListener(this)
            popup.show()
            true
        }

        listCategoryScreen.setMultiChoiceModeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_category, menu)
        menuInflater.inflate(R.menu.menu_search, menu)

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

    private fun closeSearchWindow(){
        if(searchWindow != null) {
            searchWindow!!.isIconified = true
            searchWindow!!.onActionViewCollapsed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                if (searchWindow!!.isIconified) {
                    finish()
                }
            }

            R.id.sort ->{
                ShowMainScreen.sortDialog.setActivity(this)
                ShowMainScreen.sortDialog.setElement(null)
                ShowMainScreen.sortDialog.show(supportFragmentManager, "sort")
            }

            R.id.filter ->{
                ShowMainScreen.filterDialog.setActivity(this)
                ShowMainScreen.filterDialog.setCategory(listOfCategory!!)
                ShowMainScreen.filterDialog.show(supportFragmentManager, "filter")
            }

            R.id.delete ->{
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

        }

        closeSearchWindow()

        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {

        val category = listOfCategory!!.list[menuPopupPosition]
        when (item.itemId) {

            R.id.edit -> {
                val intent = Intent(this, EditCategoryScreen::class.java)
                ShowMainScreen.actualElement = listOfCategory
                intent.putExtra("CATEGORY_NUMBER", listOfCategory!!.list.indexOf(category))
                startActivity(intent)
            }

            R.id.delete -> {
                Toast.makeText(this, getString(R.string.deleted_category) +": " + category.title,Toast.LENGTH_SHORT) .show()
                deleteCategory(category as Category)
            }

        }
        updateView()
        return true
    }

    private fun deleteCategory(category: Category){
        deleteFromTodo(category)
        Utility.deleteCategories(category)

        listOfCategory!!.list.remove(category)
    }

    private fun deleteFromTodo(category:Category){

        if(category.template == null)
        {
            for(i in 0 until category.list.size)
            {
                deleteCategory(category.list[i] as Category)
            }
        }
        else
        {
            val array = ShowMainScreen.todoList.list
            val toDel = ArrayList<Element>()

            for (elem in array) {
                if ((elem as Element).category == category) {
                    toDel.add(elem)
                }
            }

            Utility.deleteElementList(toDel)
            array.removeAll(toDel)
        }

    }

    override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {}

    override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        menuInflater.inflate(R.menu.multichoice_menu_delete,p1)
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

        when(item!!.itemId){

            R.id.action_delete ->{

                val selected = (listCategoryScreen.adapter as CategoryListViewAdapter).getSelectedList()
                Toast.makeText(this, getString(R.string.deleted_category_list) +": " + selected.size.toString(),Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    deleteCategory(selected[i] as Category)
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
