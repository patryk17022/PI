package pl.polsl.project.catalogex.edit

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.create.CreateTemplateScreen
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.display.ShowMainScreen

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class  EditTemplateScreen : CreateTemplateScreen() {

    private var oldElement :Element? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(oldElement == null) {
            oldElement = ShowMainScreen.actualElement as Element
            template = oldElement!!.copy() as Element
        }

        ratingBarElement.setIsIndicator(true)

        inputText.setText(getString(R.string.name_label))
        inputText.setActivity(this)

        updateFeatureList()

        addFeatureButton.setOnClickListener{ view -> addFeatureToElement()}

        cancleButtonTemplate.setOnClickListener{ view -> finish()}

        acceptButtonTemplate.setOnClickListener{ view ->
            ShowMainScreen.listOfTemplate.remove(oldElement!!)
            ShowMainScreen.listOfTemplate.add(template)
            setResult(RESULT_OK,null)
            finish()
        }

        editNameElement.setOnClickListener{ view ->
            inputText.setText(getString(R.string.name_label))
            inputText.show(supportFragmentManager, "textNameInput")
        }
    }

    override fun doPositiveClick(tag: String, input: String, position: Int) {
        super.doPositiveClick(tag, input, position)

        if(tag== "textNameInput") {
            template.title = input
        }

    }
}


