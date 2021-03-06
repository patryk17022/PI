package pl.polsl.project.catalogex.edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_category_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.create.CreateCategoryScreen
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.database.Utility

//Klasa odpowiedzialna za obsługę ekranu edycji kategorii
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class EditCategoryScreen : CreateCategoryScreen() {

    private var category: Category? = null

    //Metoda wywoływana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val index = intent.getIntExtra("CATEGORY_NUMBER",-1)
        category = parentCategory!!.list[index] as Category

        acceptButton.setOnClickListener{
            viewL ->

            if(!nameCategoryText.text.toString().isEmpty()) {
                category!!.title = nameCategoryText.text.toString()

                Utility.insertCategories(category!!,parentCategory!!.id)

                Toast.makeText(this,getString(R.string.edited)+": " + category!!.title, Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,getString(R.string.noCategoryName), Toast.LENGTH_SHORT).show()
            }

        }
        updateView()
    }

    //Metoda odświeżająca informacje na ekranie
    private fun updateView(){

        nameCategoryText.setText(category!!.title)

        categoryOptionRadio.isEnabled = false
        elementOptionRadio.isEnabled = false
        templateSpinner.isEnabled = false

        if(category!!.template != null){
            elementOptionRadio.isChecked = true
            templateSpinner.visibility = View.VISIBLE
            templateLabelCategory.visibility = View.VISIBLE

            val index = templateList!!.indexOf(category!!.template)+1
            templateSpinner.setSelection(index)

        }
    }
}
