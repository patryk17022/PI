package pl.polsl.project.catalogex.create

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_create_category_screen.*
import pl.polsl.project.catalogex.R
import android.widget.ArrayAdapter
import android.widget.Toast
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.display.ShowMainScreen

open class CreateCategoryScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val ADD_NEW_TEMPLATE = 1;

    var parentCategory: Category? = null
    var templateList: ArrayList<Element>? = null

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        if(pos == templateSpinner.adapter.count-1) {
            val intent = Intent(this, CreateTemplateScreen::class.java)
            startActivityForResult(intent, ADD_NEW_TEMPLATE)
        }

        acceptButton.isEnabled = (pos != 0 && pos != templateSpinner.adapter.count-1) || categoryOptionRadio.isChecked
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    private fun initializeSpinner(){

        val spinnerTemplate = ArrayList<String>()

        spinnerTemplate.add(getString(R.string.addNewTemplate))

        for(i in 0 until templateList!!.size)
        {
            spinnerTemplate.add(templateList!!.get(i).title)
        }

        spinnerTemplate.add(getString(R.string.noTemplate))

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerTemplate)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        templateSpinner.adapter = dataAdapter

        templateSpinner.setSelection(0)
        templateSpinner.onItemSelectedListener = this

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category_screen)

        if(parentCategory == null) {
            parentCategory = ShowMainScreen.actualElement as Category
            templateList = ShowMainScreen.listOfTemplate
        }

        templateLabelCategory.visibility = View.INVISIBLE
        templateSpinner.visibility = View.INVISIBLE

        optionGroup.setOnCheckedChangeListener{ radioGroup, i ->
            if(elementOptionRadio.isChecked) {
                templateLabelCategory.visibility = View.VISIBLE
                templateSpinner.visibility = View.VISIBLE
                acceptButton.isEnabled = (templateSpinner.selectedItemPosition  != 0 && templateSpinner.selectedItemPosition != templateSpinner.adapter.count-1)
            }
            else {
                templateLabelCategory.visibility = View.INVISIBLE
                templateSpinner.visibility = View.INVISIBLE
                acceptButton.isEnabled = true
            }

        }

        cancleButton.setOnClickListener{ view -> finish()}
        acceptButton.setOnClickListener{ view ->
            if(!nameCategoryText.text.toString().isEmpty()) {
                var cat = Category()
                cat.title = nameCategoryText.text.toString()

                if(categoryOptionRadio.isChecked){
                    cat.template = null
                }else {
                    cat.template = templateList!!.get(templateSpinner.selectedItemPosition - 1)
                }

                parentCategory!!.list.add(cat)
                Toast.makeText(this, getString(R.string.added_category) +": " + nameCategoryText.text.toString(),Toast.LENGTH_LONG) .show()
                finish()
            }else{
                Toast.makeText(this,getString(R.string.noCategoryName),Toast.LENGTH_SHORT).show()
            }
        }

        initializeSpinner()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ADD_NEW_TEMPLATE) {

            if (resultCode == Activity.RESULT_OK) {

                initializeSpinner()
                templateSpinner.setSelection(templateSpinner.adapter.count-2)
            }else{
                templateSpinner.setSelection(0)

            }
        }
    }

}
