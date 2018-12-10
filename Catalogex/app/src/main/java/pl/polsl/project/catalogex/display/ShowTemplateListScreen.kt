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
import pl.polsl.project.catalogex.create.CreateTemplateScreen
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.listElements.categoryList.CategoryListViewAdapter


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowTemplateListScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var displayedList: ArrayList<ListItem> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var searchWindow : SearchView? = null
    private var isSelectionMode = false

    fun updateView(text: String = ""){

        displayedList.clear()
        for(item in ShowMainScreen.listOfTemplate){

            if(text.isEmpty())
                displayedList.add(item)
            else
                if(item.title.toUpperCase().contains(text.toUpperCase()))
                    displayedList.add(item)
        }

        val adapter = CategoryListViewAdapter(layoutInflater, displayedList)
        adapter.setIsSelectionMode(isSelectionMode)
        listCategoryScreen.adapter = adapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        buttonAddList.setOnClickListener { view ->
            val intent = Intent(this, CreateTemplateScreen::class.java)
            startActivity(intent)
        }

        listCategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent: Intent?

            val elem = ShowMainScreen.listOfTemplate[ShowMainScreen.listOfTemplate.indexOf(displayedList[i])]

            intent = Intent(this, ShowElementInformationScreen::class.java)

            ShowMainScreen.actualElement = elem
            startActivity(intent)
        }

        listCategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.menu_template, popup.menu)
            menuPopupPosition = ShowMainScreen.listOfTemplate.indexOf(displayedList[i])
            popup.setOnMenuItemClickListener(this)
            popup.show()
            true
        }

        listCategoryScreen.setMultiChoiceModeListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_template, menu)
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

            R.id.delete ->{
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

        }

        closeSearchWindow()

        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {

        val temp = ShowMainScreen.listOfTemplate[menuPopupPosition]
        when (item.itemId) {

            R.id.delete -> {
                Toast.makeText(this, getString(R.string.deleted_template) +": " + temp.title,Toast.LENGTH_SHORT) .show()
                deleteTemplate(temp)
            }

        }
        updateView()
        return true
    }

    companion object {
        fun deleteTemplate(template: Element) {

            deleteOtherConnectedElement(template,ShowMainScreen.mainCategory)

            Utility.deleteTemplate(template)
            ShowMainScreen.listOfTemplate.remove(template)
        }

        private fun deleteOtherConnectedElement(template: Element, category: Category) {

            for(cat in category.list)
            {
                if(cat is Element){
                    return
                }else{
                    if((cat as Category).template != null && cat.template!!.id == template.id){

                        val array = ShowMainScreen.todoList.list
                        val toDel = ArrayList<Element>()

                        for (elem in array) {
                            if ((elem as Element).category == cat) {
                                toDel.add(elem)
                            }
                        }

                        array.removeAll(toDel)

                        category.list.remove(cat)
                    }else
                        deleteOtherConnectedElement(template,cat)
                }
            }
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
                Toast.makeText(this, getString(R.string.deleted_template_list) +": " + selected.size.toString(),Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    deleteTemplate(selected[i] as Element)
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
