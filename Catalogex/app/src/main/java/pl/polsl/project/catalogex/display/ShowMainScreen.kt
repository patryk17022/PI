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

/*TODO:
wpisywanie - otwarcie klawiatury - TextInputDialog
Edity
Gorne menusy typu sortowanie filtrowanie szukanie itd
Dodac zarzadzanie templatami
przechowywanie danych

poprawic diagram

https://material.io/tools/icons/?style=round
*/

class ShowMainScreen : AppCompatActivity() {

    companion object {
        var mainCategory = Category()
        var todoList = Category()

        var listOfTemplate : ArrayList<Element> = arrayListOf()

        var actualElement : ListItem? = mainCategory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        mainCategory.title = getString(R.string.title_activity_category_list_screen)
        todoList.title = getString(R.string.todo_button_text)


        //TEST--------------------------------------------------------------------------------------
        for(i in 0 until 10){
            var test = Category()
            test.title = "Kategoria: " + i.toString()
            mainCategory.list.add(test)
        }

        var cat = mainCategory.list.get(0) as Category

        for(i in 0 until 10){
            var test = Category()
            test.title = "POD Kategoira; " + i.toString()
            cat.list.add(test)
        }


        for(i in 1 until 10){
            for(k in 0 until 10) {
                var test = Element()
                     for(f in 0 until 10) {
                         test.addFeature(Feature(f,"Informacja:  " + f, f.toString()))
                     }
                test.title = "Element:  " + k.toString()
                test.category = mainCategory.list.get(i) as Category
                (mainCategory.list.get(i) as Category).list.add(test)
                (mainCategory.list.get(i) as Category).template = Element()
            }
        }
        //TEST--------------------------------------------------------------------------------------

    }

    fun categoryScreenShow(view: View) {
        val intent = Intent(this, ShowCategoryListScreen::class.java)
        startActivity(intent)
    }

    fun todoScreenShow(view: View) {
        val intent = Intent(this, ShowTodoScreen::class.java)
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
