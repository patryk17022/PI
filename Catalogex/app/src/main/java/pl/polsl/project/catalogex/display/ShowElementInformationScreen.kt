package pl.polsl.project.catalogex.display

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.edit.EditElementScreen
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter


class ShowElementInformationScreen : AppCompatActivity() {

    var element : Element? = null

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

        val adapter =  ElementDetailListViewAdapter(this,element!!.list,layoutInflater,this, DetailListMode.NONE_BUTTON)
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

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_options, menu)
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
                ShowMainScreen.actualElement = element!!.category
                intent.putExtra("ELEMENT_NUMBER", element!!.category!!.list.indexOf(element!!))
                startActivity(intent)
            }

            R.id.delete -> {

                if(element!!.todo == true) {
                    ShowMainScreen.todoList.list.remove(element!!)
                }else {
                    element!!.category!!.list.remove(element!!)
                }

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


