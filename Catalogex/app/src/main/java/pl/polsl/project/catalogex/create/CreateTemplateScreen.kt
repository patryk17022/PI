package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.interfaces.ElementDetailsInterface
import pl.polsl.project.catalogex.interfaces.TextInputDialogInterface
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.dialogs.TextInputDialog
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.enums.DetailListMode
import pl.polsl.project.catalogex.listElements.elementDetails.ElementDetailListViewAdapter

//Klasa odpowiedzialna za obsługę ekranu tworzenia wzoru
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
open class CreateTemplateScreen : AppCompatActivity(), ElementDetailsInterface, TextInputDialogInterface {

    protected var template = Element()
    private val inputText = TextInputDialog()

    //Metoda uruchomiana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)

        editNameElement.visibility = View.INVISIBLE
        ratingBarElement.setIsIndicator(true)

        inputText.setText(getString(R.string.name_label))
        inputText.setActivity(this)

        updateFeatureList()

        addFeatureButton.setOnClickListener{ view -> addFeatureToElement()}

        cancelButtonTemplate.setOnClickListener{ view -> finish()}

        acceptButtonTemplate.setOnClickListener{ view ->
            inputText.show(supportFragmentManager, "addTemplate")
        }
    }

    //Metoda wyświetlająca listę atrybutów na ekranie
    private fun updateFeatureList(){
        val adapter = ElementDetailListViewAdapter(template.list, layoutInflater, this, DetailListMode.EDIT_DELETE_BUTTON)
        featureList.adapter = adapter
    }

    //Metoda doodająca atrybut do wzoru
    private fun addFeatureToElement(){
        inputText.show(supportFragmentManager, "addFeature")
    }

    //Metoda wywoływana podczas naciśnięcia przycisku powrotu
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    //Metoda wywoływana podczas naciśnięcia przycisku potwierdzającego dodanie wzoru
    override fun doPositiveClick(tag: String, input: String, position: Int) {
        if(!input.isEmpty()) {
            when (tag) {
                "addFeature" -> {

                    val feature = Feature(input, getString(R.string.example_Text))

                    template.addFeature(feature)
                    updateFeatureList()

                }

                "editFeature" -> {
                    template.list[position].title = input
                }

                "addTemplate" -> {
                    template.title = input

                    Utility.insertElement(template)

                    ShowMainScreen.listOfTemplate.add(template)
                    setResult(RESULT_OK,null)
                    Toast.makeText(this, getString(R.string.added_template) + ": " + template.title, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }else{
            Toast.makeText(this,getString(R.string.noNameEntered),Toast.LENGTH_SHORT).show()
        }
    }

    //Metoda wywoływana podczas naciśnięcia przycisku usuwania
    override fun onDeleteButton(position: Int){
        template.removeFeature(position)
        updateFeatureList()
    }

    //Metoda wywoływana podczas naciśnięcia przycisku edycji
    override fun onEditButton(position: Int){
        inputText.setPosition(position)
        inputText.show(supportFragmentManager, "editFeature")
    }
}


