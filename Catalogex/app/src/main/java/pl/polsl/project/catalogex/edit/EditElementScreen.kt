package pl.polsl.project.catalogex.edit

import android.os.Bundle

import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.create.CreateElementScreen
import pl.polsl.project.catalogex.data.Element


class EditElementScreen : CreateElementScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var index = intent.getIntExtra("ELEMENT_NUMBER",-1)
        var elementOld = category!!.list.get(index) as Element
        element = elementOld.copy() as Element

        acceptButtonTemplate.setOnClickListener { view ->
            element!!.indicator = ratingBarElement.rating.toInt()
            element!!.insertValuesFrom(elementOld)
            finish()
        }

        updateView()
        inputText.dismiss()

    }
}


