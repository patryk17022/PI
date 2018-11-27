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
przechowywanie danych w bd -> work in progres x2 -> isMain skasowac i poprostu wykrywac czy jest null w cat_id, asynchroniczne queerry

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

        mainCategory.title = getString(R.string.title_activity_category_list_screen)
        todoList.title = getString(R.string.todo_button_text)

        listOfTemplate = Utility.getTemplates()
        mainCategory = Utility.getMainCategory(listOfTemplate)
        todoList = Utility.getToDoCategory(mainCategory)

        //TEST--------------------------------------------------------------------------------------

//        for(i in 0 until 10){
//            var test = Category()
//            test.title = "Kategoria: " + i.toString()
//            mainCategory.list.add(test)
//        }
//
//        var cat = mainCategory.list.get(0) as Category
//
//        for(i in 0 until 10){
//            var test = Category()
//            test.title = "POD Kategoira; " + i.toString()
//            cat.list.add(test)
//        }
//
//        for(i in 1 until 10){
//            for(k in 0 until 10) {
//                var test = Element()
//                     for(f in 0 until 10) {
//                         test.addFeature(Feature("Informacja:  " + f, f.toString()))
//                     }
//                test.title = "Element:  " + k.toString()
//                test.category = mainCategory.list.get(i) as Category
//                (mainCategory.list.get(i) as Category).list.add(test)
//                (mainCategory.list.get(i) as Category).template = Element()
//            }
//        }
        //TEST--------------------------------------------------------------------------------------

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
        //TODO zmien
        Utility.insertElementsList(listOfTemplate)
        Utility.insertCategories(mainCategory,null,true)
        Utility.insertElementsList(todoList.list as ArrayList<Element>)
        //TODO -----
//        finish()
//        System.exit(0)
    }

    override fun onResume() {
        super.onResume()
        actualElement = mainCategory
    }

}
