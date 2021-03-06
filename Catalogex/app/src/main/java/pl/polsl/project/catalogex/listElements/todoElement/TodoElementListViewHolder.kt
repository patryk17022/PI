package pl.polsl.project.catalogex.listElements.todoElement

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import pl.polsl.project.catalogex.R

//Klasa pobierająca elementy z widoku przedstawiającego elementy listy TO DO
class TodoElementListViewHolder(view: View?) {
    val tvTitle: TextView = view?.findViewById(R.id.informationText) as TextView
    val info: TextView = view?.findViewById(R.id.informationTextCategory) as TextView
    val imButton: ImageButton = view?.findViewById(R.id.dropDownListCategory) as ImageButton
    val button: Button = view?.findViewById(R.id.elementInfoButton) as Button
    val check: CheckBox = view?.findViewById(R.id.checkBox) as CheckBox
}