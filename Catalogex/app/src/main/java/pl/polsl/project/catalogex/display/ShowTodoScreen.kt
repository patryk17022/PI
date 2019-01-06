package pl.polsl.project.catalogex.display

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.ActionMode
import android.view.Menu
import kotlinx.android.synthetic.main.activity_todo_screen.*
import pl.polsl.project.catalogex.R
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.PopupMenu
import android.widget.Toast
import pl.polsl.project.catalogex.interfaces.ReturnDialogInterface
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.todoElement.TodoElementListViewAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//Klasa odpowiedzialna za obsługę ekranu przedstawiającego listę TO DO
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName")
class ShowTodoScreen : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, AbsListView.MultiChoiceModeListener, ReturnDialogInterface {

    private var todoList: Category? = null
    private var displayedList: ArrayList<Element> = ArrayList()
    private var menuPopupPosition: Int = -1
    private var isSelectionMode = false
    private var multiChoiceDelete = false
    private val REQUEST_EXPORT_PERMISSION = 1

    //Metoda odświeżająca informacje na ekranie
    @Suppress("UNCHECKED_CAST")
    private fun updateView(){

        displayedList.clear()
        for(item in todoList!!.list){
            displayedList.add(item as Element)
        }

        ShowMainScreen.sortDialog.sortTable(displayedList as ArrayList<ListItem>)
        ShowMainScreen.filterDialog.filter(displayedList as ArrayList<ListItem>)

        val adapter = TodoElementListViewAdapter( displayedList, layoutInflater, this)
        adapter.setIsSelectionMode(isSelectionMode)
        listTodo.adapter = adapter
    }

    //Metoda wywoływana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
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

    fun setListElement(position: Int) {
        menuPopupPosition = todoList!!.list.indexOf(displayedList[position])
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    //Metoda rozpatrująca wybór opcji z listy
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

            R.id.filter ->{
                ShowMainScreen.filterDialog.setActivity(this)
                ShowMainScreen.filterDialog.setCategory(ShowMainScreen.todoList)
                ShowMainScreen.filterDialog.show(supportFragmentManager, "filter")
            }

            R.id.delete ->{
                multiChoiceDelete = true
                listTodo.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.fromToDo ->{
                multiChoiceDelete = false
                listTodo.startActionMode(this as AbsListView.MultiChoiceModeListener)
            }

            R.id.exportTodo ->{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_EXPORT_PERMISSION)
                }else {
                    exportTODO()
                }
            }

        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_EXPORT_PERMISSION && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            exportTODO()
    }

    //Metoda wykorzystywana w celu eksportu listy TO DO
    @Suppress("SpellCheckingInspection")
    @SuppressLint("SimpleDateFormat")
    private fun exportTODO(){

        val sdf = SimpleDateFormat("yyyy-MM-dd_HHmmss")
        val filename = "TODO_list_" + sdf.format(Date()) + ".csv"

        try {

            val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), filename)

            if (displayedList.size > 0) {
                for(temp in ShowMainScreen.listOfTemplate) {
                    file.appendText("Id;" + getString(R.string.category) + ";" + temp.exportToString(this, true))

                    var index = 0
                    for (elem in displayedList) {
                        if(elem.category!!.template == temp) {
                            file.appendText(index.toString() + ";" + elem.category!!.title + ";" + elem.exportToString(this))
                        }
                        index += 1
                    }

                    file.appendText("\n")

                }
            }
        }
        catch (ex: Exception){
            Toast.makeText(this, getString(R.string.saved_error_file), Toast.LENGTH_SHORT) .show()
        }

        Toast.makeText(this, getString(R.string.saved_file) +": \n" + filename + "\n" + getString(R.string.in_download) , Toast.LENGTH_LONG) .show()

    }

    //Metoda rozpatrująca wybór opcji z menu
    override fun onMenuItemClick(item: MenuItem): Boolean {

        val elem = todoList!!.list[menuPopupPosition] as Element

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

    //Metoda przenosząca przedmiot z TO DO do kategorii
    private fun moveFromTODO(elem:Element){
        todoList!!.list.remove(elem)
        elem.todo = false
        Utility.insertElement(elem)
        elem.category!!.list.add(elem)
    }

    //Metoda usuwająca element
    fun deleteElement(element: Element){
        Utility.deleteElement(element)
        todoList!!.list.remove(element)
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
        (listTodo.adapter as TodoElementListViewAdapter).getSelectedList().clear()
    }

    //Metoda rozpatrująca przycisk dodawania elementów
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        val selected = (listTodo.adapter as TodoElementListViewAdapter).getSelectedList()

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
                    moveFromTODO(selected[i] as Element)
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
