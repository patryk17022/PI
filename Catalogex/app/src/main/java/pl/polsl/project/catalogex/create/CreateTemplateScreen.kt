package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.dialogs.TextInputDialogInterface
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter
import pl.polsl.project.catalogex.listElements.ElementDetailsInterface

class CreateTemplateScreen : AppCompatActivity(), ElementDetailsInterface, TextInputDialogInterface {

    val template = Element()
    val inputText = TextInputDialog()

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

        inputText.labelText = getString(R.string.name_label)
        inputText.activity = this

        updateFeatureList()

        addFeatureButton.setOnClickListener{ view -> addFeatureToElement()}

        cancleButtonTemplate.setOnClickListener{ view -> finish()}

        acceptButtonTemplate.setOnClickListener{ view ->
            inputText.show(supportFragmentManager, "addTemplate")
        }
    }

    fun addFeatureToElement(){
        inputText.show(supportFragmentManager, "addFeature")
    }

    override fun doPositiveClick(tag: String, input: String, position: Int) {
        if(!input.isEmpty()) {
            if (tag == "addFeature") {
                var id = 0

                if(template.list.size>0){
                    id = template.list.get(template.list.size-1).id!! + 1
                }

                var feature = Feature(id, input, getString(R.string.example_Text))

                template.list.add(feature)
                updateFeatureList()

            } else if (tag == "editFeature") {
                template!!.list.get(position).title = input

            } else if (tag == "addTemplate") {
                template.title = input
                ShowMainScreen.listOfTemplate.add(template)
                setResult(RESULT_OK,null)
                finish()
            }
        }else{
            Toast.makeText(this,getString(R.string.noTextEntered),Toast.LENGTH_SHORT).show()
        }
    }

    override fun doNegativeClick(tag: String, input: String, position: Int) {

    }

    override fun onDeleteButton(position: Int){
        template.list.removeAt(position)
        updateFeatureList()
    }

    override fun onEditButton(position: Int){
        inputText.position = position
        inputText.show(supportFragmentManager, "editFeature")
    }
}


