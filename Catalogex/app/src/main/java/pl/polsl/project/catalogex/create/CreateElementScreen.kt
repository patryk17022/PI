package pl.polsl.project.catalogex.create

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_category_screen.*
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.dialogs.CameraScreenChooseDialogFragment
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.dialogs.TextInputDialogInterface
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListView
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter

//TODO aktualizacja informacji o obiekcie + dodawnie zdjecia

class CreateElementScreen : AppCompatActivity(), TextInputDialogInterface {

    var listItems : ArrayList<ElementDetailListView>? = null

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        addFeatureButton.visibility = View.INVISIBLE

        //TODO lista cech
        listItems = ArrayList<ElementDetailListView>(10)

        for (i in 0 until 10) {
            listItems!!.add(ElementDetailListView(i,"title" + i,"123123"))
        }

        val adapter =  ElementDetailListViewAdapter(this,listItems!!,layoutInflater,this,DetailListMode.ADD_BUTTON)
        featureList.adapter = adapter

        elementImage.setOnClickListener{onImageScelect()}
        editNameElement.setOnClickListener{editNameButton()}
        cancleButtonTemplate.setOnClickListener{ view -> finish()}
    }


    fun onImageScelect(){
        val fm = supportFragmentManager
        val editNameDialogFragment = CameraScreenChooseDialogFragment()
        editNameDialogFragment.packageManager = packageManager
        editNameDialogFragment.image= elementImage
        editNameDialogFragment.show(fm, "photoGallery")

    }

    fun editNameButton(){
        val fm = supportFragmentManager
        val inputText = TextInputDialog()
        inputText.labelText = getString(R.string.name_label)
        inputText.show(fm, "textNameInput")
    }

    fun onAddButton(position: Int){
        val fm = supportFragmentManager
        val inputText = TextInputDialog()
        inputText.labelText = listItems!!.get(position).title
        inputText.show(fm, "textInput")
    }

    override fun doPositiveClick(tag:String, input:String){
        if(tag== "textInput")
            Toast.makeText(this,input,Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this,"NEW NAME: "+input,Toast.LENGTH_LONG).show()
    }

    override fun doNegativeClick(tag:String, input:String){

    }

}


