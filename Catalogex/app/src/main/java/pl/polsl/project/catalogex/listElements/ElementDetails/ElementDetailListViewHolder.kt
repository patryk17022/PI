package pl.polsl.project.catalogex.listElements.ElementDetails

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import pl.polsl.project.catalogex.R

class ElementDetailListViewHolder(view: View?) {
    val tvTitle: TextView
    val tvValue: TextView
    val imEdit: ImageButton
    val imDelete: ImageButton
    val imAdd: ImageButton

    init {
        this.tvTitle = view?.findViewById(R.id.titleTextViewElement) as TextView
        this.tvValue = view?.findViewById(R.id.contentTextViewElement) as TextView
        this.imEdit = view?.findViewById(R.id.editElement) as ImageButton
        this.imDelete = view?.findViewById(R.id.deleteElement) as ImageButton
        this.imAdd = view?.findViewById(R.id.editInformationElement) as ImageButton
    }
}