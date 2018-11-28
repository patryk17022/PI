package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.Utility


/*TODO:
filtrowanie elementów
wywoływanie DB nie w głównym wątku

Poprawic diagram i dopisac wymagania
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

       /* if(mainCategory.list.size == 0) {
            var f1 = Feature("1", "Przykładowy Tekst")
            var f2 = Feature("2", "Przykładowy Tekst")

            var temp = Element("Temp", null, 0)
            temp.addFeature(f1)
            temp.addFeature(f2)

            listOfTemplate.add(temp)

            var cat1 = Category()
            cat1.title = "cat1"

            var cat2 = Category()
            cat2.title = "cat1->cat2"
            cat2.template = temp

            cat1.list.add(cat2)

            mainCategory.list.add(cat1)

            var elem1 = Element("elem1", cat2, 4)
            cat2.list.add(elem1)

            var elem2 = Element("elem2", cat2, 4)
            todoList.list.add(elem2)

            f1 = Feature("1", "elem1")
            f2 = Feature("2", "elem1")

            elem1.addFeature(f1)
            elem1.addFeature(f2)

            f1 = Feature("1", "elem2")
            f2 = Feature("2", "elem2")

            elem2.addFeature(f1)
            elem2.addFeature(f2)

            Utility.insertElementsList(listOfTemplate)
            Utility.insertCategories(mainCategory,null,true)

            @Suppress("UNCHECKED_CAST")
            Utility.insertElementsList(todoList.list as ArrayList<Element>)

        }*/

    }

    fun categoryScreenShow(view: View) {
        val intent = Intent(this, ShowCategoryListScreen::class.java)
        startActivity(intent)
    }

    fun todoScreenShow(view: View) {
        println(Utility.printDB())//TODO zmien
        val intent = Intent(this, ShowTodoScreen::class.java)
        startActivity(intent)
    }

    fun templateScreenShow(view: View) {
        val intent = Intent(this, ShowTemplateListScreen::class.java)
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
