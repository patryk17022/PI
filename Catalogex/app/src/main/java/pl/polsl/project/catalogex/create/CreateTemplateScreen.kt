package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListView
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter

//TODO dodawnie wzoru - zatwierdz przycisk plus i takie tam

class CreateTemplateScreen : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        editNameElement.visibility = View.INVISIBLE
        ratingBarElement.setIsIndicator(true)

        //TODO lista cech
        val listItems = ArrayList<ElementDetailListView>(10)

        for (i in 0 until 10) {
            listItems.add(ElementDetailListView(i,"title" + i,"123123"))
        }

        val adapter =  ElementDetailListViewAdapter(this,listItems,layoutInflater, this, DetailListMode.EDIT_DELETE_BUTTON)
        featureList.adapter = adapter


        addFeatureButton.setOnClickListener{ view -> addFeatureToElement()}

        acceptButtonTemplate.setOnClickListener{ view -> finish()}
        cancleButtonTemplate.setOnClickListener{ view -> finish()}
    }

    fun addFeatureToElement(){

    }
}


