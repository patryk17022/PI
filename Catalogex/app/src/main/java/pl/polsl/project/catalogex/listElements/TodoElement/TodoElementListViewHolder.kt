package pl.polsl.project.catalogex.listElements.TodoElement

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import pl.polsl.project.catalogex.R

class TodoElementListViewHolder(view: View?) {
    val tvTitle: TextView
    val imButton: ImageButton
    val button: Button
    val check: CheckBox

    init {
        this.tvTitle = view?.findViewById(R.id.informationText) as TextView
        this.imButton = view?.findViewById(R.id.dropDownListCategory) as ImageButton
        this.button = view?.findViewById(R.id.elementInfoButton) as Button
        this.check = view?.findViewById(R.id.checkBox) as CheckBox
    }
}