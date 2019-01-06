package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.dialogs.FilterDialog
import pl.polsl.project.catalogex.dialogs.SortDialog

//Klasa odpowiedzialna za obsługę ekranu głównego menu
@Suppress("UNUSED_PARAMETER")
class ShowMainScreen : AppCompatActivity() {

    companion object {

        var listOfTemplate : ArrayList<Element> = arrayListOf()

        var mainCategory = Category()
        var todoList = Category()

        var actualElement : ListItem? = mainCategory

        val sortDialog = SortDialog()
        val filterDialog = FilterDialog()
    }

    //Metoda wywoływana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        initialize()
    }

    //Metoda inicjalizująca strukturę danych oraz połączenie z bazą danych wraz z pobraniem informacji
    private fun initialize(){
        Utility.updateAllLists()
        listOfTemplate = Utility.getTemplates()
        mainCategory = Utility.getMainCategory(listOfTemplate)
        todoList = Utility.getToDoCategory(mainCategory)

        mainCategory.title = getString(R.string.title_activity_category_list_screen)
        todoList.title = getString(R.string.todo_button_text)
    }

    fun categoryScreenShow(view: View) {
        val intent = Intent(this, ShowCategoryListScreen::class.java)
        startActivity(intent)
    }

    fun todoScreenShow(view: View) {
        val intent = Intent(this, ShowTodoScreen::class.java)
        startActivity(intent)
    }

    fun templateScreenShow(view: View) {
        val intent = Intent(this, ShowTemplateListScreen::class.java)
        startActivity(intent)
    }

    fun creditsScreenShow(view: View) {
        val intent = Intent(this, ShowCreditsScreen::class.java)
        startActivity(intent)
    }

    fun exitButton(view: View) {
        finish()
        System.exit(0)
    }

    override fun onResume() {
        super.onResume()
        actualElement = mainCategory
    }

}
