package pl.polsl.project.catalogex.display

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.dialogs.ShowPhotoDialog
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.enums.DetailListMode
import pl.polsl.project.catalogex.listElements.elementDetails.ElementDetailListViewAdapter

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowElementInformationScreen : AppCompatActivity() {

    private var element : Element? = null

    fun updateView(){

        supportActionBar!!.title = element!!.title
        ratingBarElement.rating = element!!.indicator.toFloat()

        if(element!!.todo == true){
            categoryText.text = "TODO: " + element!!.category!!.title
        }else {
            categoryText.text = element!!.category!!.title
        }

        elementText.text = element!!.title

        if(element!!.image != null){
            elementImage.setImageBitmap(element!!.image)
        } else {
            elementImage.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.image))
        }

        val adapter = ElementDetailListViewAdapter(element!!.list, layoutInflater, this, DetailListMode.NONE_BUTTON)
        featureList.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_template_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(element == null) element = ShowMainScreen.actualElement as Element

        editNameElement.visibility = View.INVISIBLE
        addFeatureButton.visibility = View.INVISIBLE
        acceptButtonTemplate.visibility = View.INVISIBLE
        cancleButtonTemplate.visibility = View.INVISIBLE
        ratingBarElement.setIsIndicator(true)

        elementImage.setOnClickListener{
            var dialog = ShowPhotoDialog()
            dialog.setArguments(windowManager,element!!.image)
            dialog.show(supportFragmentManager, "photo")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        if(element!!.todo)
            menuInflater.inflate(R.menu.menu_todo_popup, menu)
        else
            menuInflater.inflate(R.menu.menu_element_popup, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.edit -> {
                val intent = Intent(this, EditElementScreen::class.java)
                ShowMainScreen.actualElement = ShowMainScreen.todoList

                if(element!!.todo)
                    intent.putExtra("ELEMENT_NUMBER", ShowMainScreen.todoList.list.indexOf(element!!))
                else
                    intent.putExtra("ELEMENT_NUMBER", element!!.category!!.list.indexOf(element!!))

                startActivity(intent)
            }

            R.id.delete -> {
                if(element!!.todo) {
                    ShowMainScreen.todoList.list.remove(element!!)
                }else {
                    element!!.category!!.list.remove(element!!)
                }

                finish()
            }

            R.id.fromToDo -> {
                ShowMainScreen.todoList.list.remove(element!!)
                element!!.category!!.list.add(element!!)
                element!!.todo = false
                Toast.makeText(this, getString(R.string.moved) +": " + element!!.title, Toast.LENGTH_SHORT) .show()
                finish()
            }

            R.id.addToDoList ->{
                element!!.category!!.list.remove(element!!)
                ShowMainScreen.todoList.list.add(element!!)
                element!!.todo = true
                Toast.makeText(this, getString(R.string.moved) +": " + element!!.title, Toast.LENGTH_SHORT) .show()
                finish()
            }

            android.R.id.home -> {
                finish()
            }

        }
        updateView()
        return true
    }
}


