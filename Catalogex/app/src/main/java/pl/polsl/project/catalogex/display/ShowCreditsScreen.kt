package pl.polsl.project.catalogex.display

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.project.catalogex.R

class ShowCreditsScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits_screen)
    }

    fun backButton(view: View) {
        finish()
    }
}