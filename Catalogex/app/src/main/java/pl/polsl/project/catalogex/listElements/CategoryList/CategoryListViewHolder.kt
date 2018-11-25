package pl.polsl.project.catalogex.listElements.CategoryList

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import pl.polsl.project.catalogex.R

class CategoryListViewHolder(view: View?){
    val tvTitle: TextView
    val checkBox: CheckBox

    init {
        this.tvTitle = view?.findViewById(R.id.informationText) as TextView
        this.checkBox = view?.findViewById(R.id.selectionBox) as CheckBox
    }
}