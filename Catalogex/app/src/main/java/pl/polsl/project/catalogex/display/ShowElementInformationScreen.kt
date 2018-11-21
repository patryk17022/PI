package pl.polsl.project.catalogex.display

import android.content.Intent
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
        val adapter =  ElementDetailListViewAdapter(this,element!!.list,layoutInflater,this, DetailListMode.NONE_BUTTON)
        featureList.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_template_screen)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(element == null) element = ShowMainScreen.actualElement as Element

        supportActionBar!!.title = element!!.title

        editNameElement.visibility = View.INVISIBLE
        addFeatureButton.visibility = View.INVISIBLE
        acceptButtonTemplate.visibility = View.INVISIBLE
        cancleButtonTemplate.visibility = View.INVISIBLE
        ratingBarElement.setIsIndicator(true)
        ratingBarElement.numStars = element!!.indicator
        categoryText.text = element!!.category!!.title
        elementText.text = element!!.title

        if(element!!.image != null){
            elementImage.setImageBitmap(element!!.image)
        }

        if(element == null) element = ShowMainScreen.actualElement as Element
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
                ShowMainScreen.actualElement = element!!
                startActivityForResult(intent,1)
            }

            R.id.delete -> {
                element!!.category!!.list.remove(element!!)
                finish()
            }

            android.R.id.home -> {
                finish()
            }

        }
        return true
    }

}


