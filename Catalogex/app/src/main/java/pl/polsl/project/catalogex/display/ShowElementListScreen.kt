package pl.polsl.project.catalogex.display

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.categoryList.CategoryListViewAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

//Klasa odpowiedzialna za obsługę ekranu listy przedmiotów
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName")
class ShowElementListScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var listOfElements : Category? = null
    private var displayedList: ArrayList<ListItem> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var searchWindow : SearchView? = null
    private var isSelectionMode = false
    private var multiChoiceDelete = false
    private val REQUEST_EXPORT_PERMISSION = 1

    //Metoda odświeżająca informacje na ekranie
    fun updateView(text: String = ""){

        displayedList.clear()
        for(item in listOfElements!!.list){

            if(text.isEmpty()) displayedList.add(item) else
                if(item.title.toUpperCase().contains(text.toUpperCase()))
                    displayedList.add(item)
        }

        ShowMainScreen.sortDialog.sortTable(displayedList)
        ShowMainScreen.filterDialog.filter(displayedList)

        val adapter = CategoryListViewAdapter(layoutInflater, displayedList)
        adapter.setIsSelectionMode(isSelectionMode)
        listCategoryScreen.adapter = adapter

        supportActionBar!!.title = listOfElements!!.title
    }

    //Metoda wywoływana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_element_list_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(listOfElements == null) listOfElements = ShowMainScreen.actualElement as Category

        listCategoryScreen.setOnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.menuInflater.inflate(R.menu.menu_element_popup, popup.menu)
            menuPopupPosition = listOfElements!!.list.indexOf(displayedList[i])
            popup.setOnMenuItemClickListener(this)
            popup.show()
            true
        }

        listCategoryScreen.setOnItemClickListener{ adapterView, view, i, l ->
            val intent = Intent(this, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = listOfElements!!.list[listOfElements!!.list.indexOf(displayedList[i])]
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

    //Metoda rozpatrująca wybór opcji z listy rozwijanej
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                if (searchWindow!!.isIconified) {
                    finish()
                }
            }

            R.id.sort ->{
                ShowMainScreen.sortDialog.setActivity(this)
                ShowMainScreen.sortDialog.setElement(listOfElements!!.template)
                ShowMainScreen.sortDialog.show(supportFragmentManager, "sort")
            }

            R.id.filter ->{
                ShowMainScreen.filterDialog.setActivity(this)
                ShowMainScreen.filterDialog.setCategory(listOfElements!!)
                ShowMainScreen.filterDialog.show(supportFragmentManager, "filter")
            }

            R.id.delete ->{
                multiChoiceDelete = true
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.addToDoList ->{
                multiChoiceDelete = false
                listCategoryScreen.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.exportCategory ->{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_EXPORT_PERMISSION)
                }else {
                    exportCategory()
                }
            }

        }

        closeSearchWindow()

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_EXPORT_PERMISSION && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            exportCategory()
    }

    //Metoda wykorzystywana w celu eksportu listy przedmiotów
    @Suppress("SpellCheckingInspection")
    @SuppressLint("SimpleDateFormat")
    private fun exportCategory(){

        val sdf = SimpleDateFormat("yyyy-MM-dd_HHmmss")
        val filename = listOfElements!!.title + "_" + sdf.format(Date()) + ".csv"

        try {

            val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), filename)

            if (displayedList.size > 0) {
                file.writeText("Id;" + (displayedList[0] as Element).exportToString(this, true))

                var index = 0
                for (elem in displayedList) {
                    file.appendText(index.toString() + ";" + (elem as Element).exportToString(this))
                    index += 1
                }
            }
        }
        catch (ex: Exception){
            Toast.makeText(this, getString(R.string.saved_error_file), Toast.LENGTH_SHORT) .show()
        }

        Toast.makeText(this, getString(R.string.saved_file) +": \n" + filename + "\n" + getString(R.string.in_download) , Toast.LENGTH_LONG) .show()

    }

    private fun closeSearchWindow(){
        if(searchWindow != null) {
            searchWindow!!.isIconified = true
            searchWindow!!.onActionViewCollapsed()
        }
    }

    //Metoda rozpatrująca wybór opcji z menu
    override fun onMenuItemClick(item: MenuItem): Boolean {

        val elem = listOfElements!!.list[menuPopupPosition] as Element
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

    //Metoda przenosi przedmiot do listy TO DO
    private fun moveToTODO(elem:Element){
        listOfElements!!.list.remove(elem)
        elem.todo = true
        Utility.insertElement(elem)
        ShowMainScreen.todoList.list.add(elem)
    }

    //Metoda usuwająca przedmiot
    fun deleteElement(element: Element){
        Utility.deleteElement(element)
        listOfElements!!.list.remove(element)
    }

    override fun onItemCheckedStateChanged(p0: ActionMode?, p1: Int, p2: Long, p3: Boolean) {}

    override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {

        if(multiChoiceDelete) {
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

    //Metoda rozpatrująca przycisk dodawania elementów
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        val selected = (listCategoryScreen.adapter as CategoryListViewAdapter).getSelectedList()

        when(item!!.itemId){

            R.id.action_delete ->{
                Toast.makeText(this, getString(R.string.deleted_element_list) +": " + selected.size.toString(), Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    deleteElement(selected[i] as Element)
                }
            }

            R.id.action_accept ->{
                Toast.makeText(this, getString(R.string.moved) +": " + selected.size.toString(), Toast.LENGTH_SHORT) .show()

                for(i in 0 until selected.size){
                    moveToTODO(selected[i] as Element)
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
