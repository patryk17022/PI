package pl.polsl.project.catalogex.edit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.create.CreateElementScreen
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.database.Utility

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class EditElementScreen : CreateElementScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val index = intent.getIntExtra("ELEMENT_NUMBER",-1)
        val elementOld = category!!.list[index] as Element
        element = elementOld.copy() as Element
        element!!.id = elementOld.id

        acceptButtonTemplate.setOnClickListener { view ->
            element!!.indicator = ratingBarElement.rating.toInt()

            for(feat in elementOld.list){
                Utility.deleteFeature(feat)
            }

            element!!.insertValuesInto(elementOld)

            Utility.insertElement(element!!)

            Toast.makeText(this,getString(R.string.edited)+": " + element!!.title, Toast.LENGTH_SHORT).show()
            finish()
        }

        updateView()
        inputText.dismiss()
    }

    override fun updateView() {
        super.updateView()
        if(element!!.image != null){
            elementImage.setImageBitmap(element!!.image)
            elementImage.scaleType = ImageView.ScaleType.FIT_CENTER
            deletePhotoButton.visibility = View.VISIBLE
        } else {
            elementImage.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.image))
            elementImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }
}


