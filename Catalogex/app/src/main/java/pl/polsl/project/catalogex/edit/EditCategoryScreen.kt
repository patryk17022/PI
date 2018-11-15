package pl.polsl.project.catalogex.edit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_category_screen.*
import pl.polsl.project.catalogex.R
import android.widget.RadioButton


class EditCategoryScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category_screen)

        for (i in 0 until optionGroup.getChildCount()) {
            (optionGroup.getChildAt(i) as RadioButton).isEnabled = false
        }

        if(elementOptionRadio.isChecked) {
            templateLabelCategory.visibility = View.VISIBLE
            templateSpinner.visibility = View.VISIBLE
        }
        else {
            templateLabelCategory.visibility = View.INVISIBLE
            templateSpinner.visibility = View.INVISIBLE
        }

        cancleButton.setOnClickListener{ view -> finish()}
    }
}
