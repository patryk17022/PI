package pl.polsl.project.catalogex.edit

import android.os.Bundle
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

        var index = intent.getIntExtra("ELEMENT_NUMBER",-1)
        var elementOld = category!!.list.get(index) as Element
        element = elementOld.copy() as Element
        element!!.id = elementOld!!.id

        acceptButtonTemplate.setOnClickListener { view ->
            element!!.indicator = ratingBarElement.rating.toInt()
            element!!.insertValuesInto(elementOld)

            Utility.insertElement(element!!)

            Toast.makeText(this,getString(R.string.edited)+": " + element!!.title, Toast.LENGTH_SHORT).show()

            finish()
        }

        updateView()
        inputText.dismiss()
    }
}


