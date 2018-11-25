package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.`interface`.ElementDetailsInterface
import pl.polsl.project.catalogex.`interface`.TextInputDialogInterface
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.enums.DetailListMode
import pl.polsl.project.catalogex.listElements.elementDetails.ElementDetailListViewAdapter

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
open class CreateTemplateScreen : AppCompatActivity(), ElementDetailsInterface, TextInputDialogInterface {

    protected var template = Element()
    protected val inputText = TextInputDialog()

    fun updateFeatureList(){
        val adapter = ElementDetailListViewAdapter(template.list, layoutInflater, this, DetailListMode.EDIT_DELETE_BUTTON)
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

        inputText.setText(getString(R.string.name_label))
        inputText.setActivity(this)

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
            when (tag) {
                "addFeature" -> {
                    var id = 0

                    if(template.list.size>0){
                        id = template.list.get(template.list.size-1).id + 1
                    }

                    var feature = Feature(id, input, getString(R.string.example_Text))

                    template.list.add(feature)
                    updateFeatureList()

                }

                "editFeature" -> {
                    template.list.get(position).title = input
                }

                "addTemplate" -> {
                    template.title = input
                    ShowMainScreen.listOfTemplate.add(template)
                    setResult(RESULT_OK,null)
                    Toast.makeText(this, getString(R.string.added_template) + ": " + template.title, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }else{
            Toast.makeText(this,getString(R.string.noNameEntered),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteButton(position: Int){
        template.list.removeAt(position)
        updateFeatureList()
    }

    override fun onEditButton(position: Int){
        inputText.setPosition(position)
        inputText.show(supportFragmentManager, "editFeature")
    }
}


