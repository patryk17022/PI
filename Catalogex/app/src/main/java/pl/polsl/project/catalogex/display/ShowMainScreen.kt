package pl.polsl.project.catalogex.display

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.polsl.project.catalogex.R

//TODO: ogarnac androidmanifest (stringi) i plik strings ! do tego sprawdzicz czyu id sa rowniez oraz dodac ekran TODO list
class ShowMainScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }
}
