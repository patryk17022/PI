package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.project.catalogex.R

/*TODO:

1. poruszanie sie miedzy ekranami
2. sprawdzicz czy wszystko jest zaimplementowane
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
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

}
