package pl.polsl.project.catalogex.create

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.interfaces.ElementDetailsInterface
import pl.polsl.project.catalogex.interfaces.ImageTakenInterface
import pl.polsl.project.catalogex.interfaces.TextInputDialogInterface
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.dialogs.CameraScreenChooseDialogFragment
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.enums.DetailListMode
import pl.polsl.project.catalogex.listElements.elementDetails.ElementDetailListViewAdapter

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
open class CreateElementScreen : AppCompatActivity(), TextInputDialogInterface, ElementDetailsInterface, ImageTakenInterface {

    protected var category: Category? = null
    protected var element: Element? = null
    protected val inputText = TextInputDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        if(category == null) category = ShowMainScreen.actualElement as Category

        addFeatureButton.visibility = View.INVISIBLE

        if(category!! != ShowMainScreen.todoList) {
            element = category!!.template!!.copy() as Element
            element!!.category = category!!
        } else{
            element = Element()
        }

        inputText.setActivity(this)

        updateView()
        editNameButton()

        acceptButtonTemplate.setOnClickListener { view ->
            if(!elementText.text.toString().isEmpty()) {
                element!!.indicator = ratingBarElement.rating.toInt()

                Utility.insertElement(element!!)

                category!!.list.add(element!!)
                Toast.makeText(this, getString(R.string.added_element) + ": " + element!!.title, Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,getString(R.string.noElementName),Toast.LENGTH_SHORT).show()
            }
        }

        deletePhotoButton.setOnClickListener{ view ->
            element!!.image = null
            elementImage.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.add_photo))
            elementImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
            deletePhotoButton.visibility = View.INVISIBLE
        }

        elementImage.setOnClickListener{onImageSelect()}
        editNameElement.setOnClickListener{editNameButton()}
        cancleButtonTemplate.setOnClickListener{view -> finish()}
    }

    open fun updateView(){
        val adapter = ElementDetailListViewAdapter(element!!.list, layoutInflater, this, DetailListMode.ADD_BUTTON)
        featureList.adapter = adapter
        elementText.text = element!!.title
        categoryText.text = category!!.title
        ratingBarElement.rating = element!!.indicator.toFloat()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun imageHasBeenTaken(bitmap: Bitmap) {
        element!!.image = bitmap
        elementImage.setImageBitmap(bitmap)
        elementImage.scaleType = ImageView.ScaleType.FIT_CENTER
        deletePhotoButton.visibility = View.VISIBLE
    }

    private fun onImageSelect(){
        val choosePhoto = CameraScreenChooseDialogFragment()
        choosePhoto.setArguments(this,packageManager)
        choosePhoto.show(supportFragmentManager, "photoGallery")
    }

    private fun editNameButton(){
        inputText.setText(getString(R.string.name_label))
        inputText.show(supportFragmentManager, "textNameInput")
    }

    override fun onAddButton(position: Int){
        inputText.setText(element!!.list.get(position).title)
        inputText.setPosition(position)
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

}


