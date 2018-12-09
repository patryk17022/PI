package pl.polsl.project.catalogex.display

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.dialogs.ShowPhotoDialog
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.enums.DetailListMode
import pl.polsl.project.catalogex.listElements.elementDetails.ElementDetailListViewAdapter

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowElementInformationScreen : AppCompatActivity() {

    private var element : Element? = null

    private fun updateView(){

        supportActionBar!!.title = element!!.title
        ratingBarElement.rating = element!!.indicator.toFloat()

        when {
            element!!.todo -> categoryText.text = "TODO: " + element!!.category!!.title
            element!!.category == null -> categoryText.text = getString(R.string.template_button_text)
            else -> categoryText.text = element!!.category!!.title
        }

        elementText.text = element!!.title

        if(element!!.image != null){
            elementImage.setImageBitmap(element!!.image)
            elementImage.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            elementImage.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.image))
            elementImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
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
        acceptButtonTemplate.visibility = View.GONE
        cancleButtonTemplate.visibility = View.GONE
        ratingBarElement.setIsIndicator(true)

        elementImage.setOnClickListener{
            var dialog = ShowPhotoDialog()
            dialog.setArguments(windowManager,element!!.image)
            dialog.show(supportFragmentManager, "photo")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        when {
            element!!.todo -> menuInflater.inflate(R.menu.menu_todo_popup, menu)
            element!!.category == null -> menuInflater.inflate(R.menu.menu_template, menu)
            else -> menuInflater.inflate(R.menu.menu_element_popup, menu)
        }
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


                if (element!!.todo) {
                    ShowMainScreen.actualElement = ShowMainScreen.todoList
                    intent.putExtra("ELEMENT_NUMBER", ShowMainScreen.todoList.list.indexOf(element!!))
                }
                else {
                    ShowMainScreen.actualElement = element!!.category
                    intent.putExtra("ELEMENT_NUMBER", element!!.category!!.list.indexOf(element!!))
                }

                startActivity(intent)

            }

            R.id.delete -> {

                if(element!!.category != null)
                {
                    if (element!!.todo) {
                        ShowMainScreen.todoList.list.remove(element!!)
                    } else {
                        element!!.category!!.list.remove(element!!)
                    }
                    Utility.deleteElement(element!!)

                }else{
                    ShowTemplateListScreen.deleteTemplate(element!!)
                }

                finish()
            }

            R.id.fromToDo -> {
                ShowMainScreen.todoList.list.remove(element!!)
                element!!.todo = false
                Utility.insertElement(element!!)
                element!!.category!!.list.add(element!!)
                Toast.makeText(this, getString(R.string.moved) +": " + element!!.title, Toast.LENGTH_SHORT) .show()
                finish()
            }

            R.id.addToDoList ->{
                element!!.category!!.list.remove(element!!)
                element!!.todo = true
                Utility.insertElement(element!!)
                ShowMainScreen.todoList.list.add(element!!)
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


