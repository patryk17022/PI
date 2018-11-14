package pl.polsl.project.catalogex.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.dialogs.CameraScreenChooseDialogFragment


//TODO: schowac przyciski do usowania i dodawania cech
class CreateElementScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template_screen)
        elementImage.setOnClickListener{onImageScelect()}
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

    fun onImageScelect(){
        val fm = supportFragmentManager
        val editNameDialogFragment = CameraScreenChooseDialogFragment()
        editNameDialogFragment.show(fm, "Name This")
    }

}


