package pl.polsl.project.catalogex.create

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.dialogs.CameraScreenChooseDialogFragment
import pl.polsl.project.catalogex.dialogs.ImageTakenInterface
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.dialogs.TextInputDialogInterface
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.listElements.DetailListMode
import pl.polsl.project.catalogex.listElements.ElementDetailListViewAdapter
import pl.polsl.project.catalogex.listElements.ElementDetailsInterface

class CreateElementScreen : AppCompatActivity(), TextInputDialogInterface, ElementDetailsInterface, ImageTakenInterface {

    var category: Category? = null
    var element: Element? = null
    val inputText = TextInputDialog()

    fun updateView(){
        val adapter =  ElementDetailListViewAdapter(this,element!!.list,layoutInflater,this,DetailListMode.ADD_BUTTON)
        featureList.adapter = adapter
        elementText.text = element!!.title
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        if(category == null) category = ShowMainScreen.actualElement as Category

        addFeatureButton.visibility = View.INVISIBLE

        element = category!!.template!!.copy()
        element!!.category = category
        inputText.activity = this

        updateView()

        elementImage.setOnClickListener{onImageScelect()}
        editNameElement.setOnClickListener{editNameButton()}
        cancleButtonTemplate.setOnClickListener{ view -> finish()}

        editNameButton()

        acceptButtonTemplate.setOnClickListener { view ->
            element!!.indicator = ratingBarElement.rating.toInt()
            category!!.list.add(element!!)
            finish()
        }

        deletePhotoButton.setOnClickListener{ view ->
            element!!.image = null
            elementImage.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.add_photo))
            elementImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
            deletePhotoButton.visibility = View.INVISIBLE
        }

    }

    override fun imageHasBeenTaken(bitmap: Bitmap) {
        element!!.image = bitmap
        elementImage.setImageBitmap(bitmap)
        elementImage.scaleType = ImageView.ScaleType.FIT_CENTER
        deletePhotoButton.visibility = View.VISIBLE

    }


    fun onImageScelect(){
        val editNameDialogFragment = CameraScreenChooseDialogFragment()
        editNameDialogFragment.packageManager = packageManager
        editNameDialogFragment.activity= this
        editNameDialogFragment.show(supportFragmentManager, "photoGallery")

    }

    fun editNameButton(){
        inputText.labelText = getString(R.string.name_label)
        inputText.show(supportFragmentManager, "textNameInput")
    }

    override fun onAddButton(position: Int){
        inputText.labelText = element!!.list.get(position).title
        inputText.position=position
        inputText.show(supportFragmentManager, "textInput")

    }

    override fun doPositiveClick(tag:String, input:String, position: Int){
        if(tag== "textInput") {
            element!!.list.get(position).detail = input
        }
        else {
            element!!.title = input
        }

        updateView()
    }

    override fun doNegativeClick(tag:String, input:String, position: Int){

    }

}


