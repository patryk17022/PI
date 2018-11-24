package pl.polsl.project.catalogex.listElements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.display.ShowCategoryListScreen


private class CategoryListViewHolder(view: View?){
    val tvTitle: TextView
    val checkBox: CheckBox

    init {
        this.tvTitle = view?.findViewById(R.id.informationText) as TextView
        this.checkBox = view?.findViewById(R.id.selectionBox) as CheckBox
    }
}

class CategoryListView : BaseAdapter {

    private var categoryList : ArrayList<ListItem>
    private var layoutInflater : LayoutInflater
    var selectedList: ArrayList<ListItem> = ArrayList()

    constructor(layoutInflater : LayoutInflater, categoryList: ArrayList<ListItem>) : super() {
        this.categoryList = categoryList
        this.layoutInflater = layoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: CategoryListViewHolder

        if (convertView == null) {
            view = layoutInflater?.inflate(R.layout.element_checkbox_list, parent, false)
            vh = CategoryListViewHolder(view)
            view?.tag = vh

        } else {
            view = convertView
            vh = view.tag as CategoryListViewHolder
        }

        vh.tvTitle.text = categoryList[position].title

        if(ShowCategoryListScreen.isSelectionMode){
            vh.checkBox.visibility = View.VISIBLE
        }else{
            vh.checkBox.visibility = View.GONE
        }

        vh.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                selectedList.add(categoryList.get(position))
            } else{
                selectedList.remove(categoryList.get(position))
            }
        }

        return view
    }

    override fun getItem(p0: Int): Any {
        return categoryList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return categoryList.size
    }


}