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


/*TODO:
filtrowanie
Dodac zarzadzanie templatami (edit template nie testowane)
 asynchroniczne queerry + usuwanie

Poprawic diagram
*/

@Suppress("UNUSED_PARAMETER")
class ShowMainScreen : AppCompatActivity() {

    companion object {

        var listOfTemplate : ArrayList<Element> = arrayListOf()

        var mainCategory = Category()
        var todoList = Category()

        var actualElement : ListItem? = mainCategory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

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
        Utility.printDB()//TODO zmien
        val intent = Intent(this, ShowTodoScreen::class.java)
        startActivity(intent)
    }

    fun creditsScreenShow(view: View) {
        Utility.deleteDatabaseFile()    //TODO usun
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
