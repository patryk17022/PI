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


3. poprawic diagram
4. zrobic zrzuty ekranu
5. zaczac obkadzac wszystko !
6. TESTY


CreateTemplateScreen
wszystkie displaye
wszystkie edity

zbudowac system zarzadzania danymi
podlaczyc pod ekrany
przechowywanie na dysku

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



        for(i in 0 until 10){
            var test = Category()
            test.title = "KUPA" + i.toString()
            mainCategory.list.add(test)
        }

        var cat = mainCategory.list.get(0) as Category

        for(i in 0 until 10){
            var test = Category()
            test.title = "POD KUPA " + i.toString()
            cat.list.add(test)
        }


        for(i in 1 until 10){
            for(k in 0 until 10) {
                var test = Element()
                     for(f in 0 until 10) {
                         test.addFeature(Feature(f,"Feature " + i, i.toString()))
                     }
                test.title = "POD KUPA ELEMENT " + k.toString()
                test.category = mainCategory.list.get(i) as Category
                (mainCategory.list.get(i) as Category).list.add(test)
                (mainCategory.list.get(i) as Category).template = Element()
            }
        }



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
