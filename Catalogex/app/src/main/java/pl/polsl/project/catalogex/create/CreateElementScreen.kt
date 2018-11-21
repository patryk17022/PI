package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.dialogs.CameraScreenChooseDialogFragment
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.dialogs.TextInputDialogInterface
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter
import pl.polsl.project.catalogex.listElements.ElementDetailsInterface

//TODO aktualizacja informacji o obiekcie + dodawnie zdjecia

class CreateElementScreen : AppCompatActivity(), TextInputDialogInterface, ElementDetailsInterface {

    private val fm = supportFragmentManager
    var listItems : ArrayList<Feature>? =  ArrayList<Feature>()
    var nameItem: String=""

    fun updateView(){
        val adapter =  ElementDetailListViewAdapter(this,listItems!!,layoutInflater,this,DetailListMode.ADD_BUTTON)
        featureList.adapter = adapter
        elementText.text=nameItem
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        addFeatureButton.visibility = View.INVISIBLE


        for (i in 0 until 10) {
            listItems!!.add(Feature(i,"title" + i,"123123"))
        }


        updateView()

        elementImage.setOnClickListener{onImageScelect()}
        editNameElement.setOnClickListener{editNameButton()}
        cancleButtonTemplate.setOnClickListener{ view -> finish()}

        editNameButton()
    }


    fun onImageScelect(){
        val editNameDialogFragment = CameraScreenChooseDialogFragment()
        editNameDialogFragment.packageManager = packageManager
        editNameDialogFragment.image= elementImage
        editNameDialogFragment.show(fm, "photoGallery")

    }

    fun editNameButton(){
        val inputText = TextInputDialog()
        inputText.labelText = getString(R.string.name_label)
        inputText.show(fm, "textNameInput")
    }

    override fun onAddButton(position: Int){
        val inputText = TextInputDialog()
        inputText.labelText = listItems!!.get(position).title
        inputText.position=position
        inputText.show(fm, "textInput")

    }

    override fun doPositiveClick(tag:String, input:String, position: Int){
        if(tag== "textInput") {
            listItems!!.get(position).detail = input
        }
        else {
            nameItem = input
        }

        updateView()
    }

    override fun doNegativeClick(tag:String, input:String, position: Int){

    }

}


