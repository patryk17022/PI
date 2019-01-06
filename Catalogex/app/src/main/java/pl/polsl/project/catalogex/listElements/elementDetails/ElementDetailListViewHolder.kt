package pl.polsl.project.catalogex.listElements.elementDetails

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import pl.polsl.project.catalogex.R

//Klasa pobierająca elementy z widoku przedstawiającego elementy listy atrybutów dla przedmiotu
class ElementDetailListViewHolder(view: View?) {
    val tvTitle: TextView = view?.findViewById(R.id.titleTextViewElement) as TextView
    val tvValue: TextView = view?.findViewById(R.id.contentTextViewElement) as TextView
    val imEdit: ImageButton = view?.findViewById(R.id.editElement) as ImageButton
    val imDelete: ImageButton = view?.findViewById(R.id.deleteElement) as ImageButton
    val imAdd: ImageButton = view?.findViewById(R.id.editInformationElement) as ImageButton
}