package pl.polsl.project.catalogex.listElements.todoElement

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import pl.polsl.project.catalogex.R

class TodoElementListViewHolder(view: View?) {
    val tvTitle: TextView = view?.findViewById(R.id.informationText) as TextView
    val imButton: ImageButton = view?.findViewById(R.id.dropDownListCategory) as ImageButton
    val button: Button = view?.findViewById(R.id.elementInfoButton) as Button
    val check: CheckBox = view?.findViewById(R.id.checkBox) as CheckBox

}