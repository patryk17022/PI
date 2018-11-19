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

//TODO przycisk zatwierdz i dodawanie wzoru

class CreateCategoryScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val ADD_NEW_TEMPLATE = 1;

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        if(pos == templateSpinner.adapter.count-1)
        {
            val intent = Intent(this, CreateTemplateScreen::class.java)
            startActivityForResult(intent,ADD_NEW_TEMPLATE)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    private fun initializeSpinner(){

        val templatesList = ArrayList<String>()

        //TODO: wypełnić spinnera
        templatesList.add("Automobile");
        templatesList.add("Business Services");


        templatesList.add(getString(R.string.addNewTemplate))

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, templatesList)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        templateSpinner.adapter = dataAdapter

        templateSpinner.setSelection(0)
        templateSpinner.onItemSelectedListener = this

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category_screen)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        templateLabelCategory.visibility = View.INVISIBLE
        templateSpinner.visibility = View.INVISIBLE

        optionGroup.setOnCheckedChangeListener{ radioGroup, i ->
            if(elementOptionRadio.isChecked) {
                templateLabelCategory.visibility = View.VISIBLE
                templateSpinner.visibility = View.VISIBLE
            }
            else {
                templateLabelCategory.visibility = View.INVISIBLE
                templateSpinner.visibility = View.INVISIBLE
            }

        }


        cancleButton.setOnClickListener{ view -> finish()}

        initializeSpinner()
    }

}
