package pl.polsl.project.catalogex.display

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu

import kotlinx.android.synthetic.main.activity_category_element_list_screen.*
import pl.polsl.project.catalogex.R


//TODO: przytrzymaj element zeby usubac i edytowac i dodac do elementy listy todo
class ShowElementListScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_element_list_screen)

        buttonAddList.setOnClickListener { view ->
            Snackbar.make(view, "SOME ACTION", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.element_menu_lists, menu)
        inflater.inflate(R.menu.element_menu_search, menu)

        return true
    }
}
