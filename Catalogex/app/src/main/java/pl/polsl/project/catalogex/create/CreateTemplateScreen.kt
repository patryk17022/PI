package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter
import pl.polsl.project.catalogex.listElements.ElementDetailsInterface

//TODO dodawnie wzoru - zatwierdz przycisk plus i takie tam

class CreateTemplateScreen : AppCompatActivity(), ElementDetailsInterface {

    val template = Element()


    fun updateFeatureList(){

        val adapter =  ElementDetailListViewAdapter(this, template.list,layoutInflater, this, DetailListMode.EDIT_DELETE_BUTTON)
        featureList.adapter = adapter

    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        editNameElement.visibility = View.INVISIBLE
        ratingBarElement.setIsIndicator(true)

        updateFeatureList()

        addFeatureButton.setOnClickListener{ view -> addFeatureToElement()}

        acceptButtonTemplate.setOnClickListener{ view -> finish()}
        cancleButtonTemplate.setOnClickListener{ view -> finish()}
    }

    fun addFeatureToElement(){
        var feature = Feature(0,"","")

      //  feature.id = listItems.get(listItems.size-1).id!! + 1
       // feature.detail = getString(R.string.example_Text)

        //TODO TU skonczylem dodawnie elementu nazwa itd

        feature.title = getString(R.string.example_Text)
        feature.detail = getString(R.string.example_Text)

        template.list.add(feature)
        updateFeatureList()
    }

    override fun onDeleteButton(position: Int){
        template.list.removeAt(position)
        updateFeatureList()
    }

    override fun onEditButton(position: Int){
        template.list.removeAt(position)
        updateFeatureList()
    }
}


