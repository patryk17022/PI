package pl.polsl.project.catalogex.listElements.categoryList

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import pl.polsl.project.catalogex.R

//Klasa pobierająca elementy z widoku przedstawiającego elementy listy kategorii
class CategoryListViewHolder(view: View?){
    val tvTitle: TextView = view?.findViewById(R.id.informationText) as TextView
    val checkBox: CheckBox = view?.findViewById(R.id.selectionBox) as CheckBox
}