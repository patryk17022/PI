package pl.polsl.project.catalogex.display

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import pl.polsl.project.catalogex.R


//TODO: schowac przyciski do edycji i usowania strzalka do powrotu
class ShowElementInformationScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_options, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {


            }

        }
        return true
    }
}

