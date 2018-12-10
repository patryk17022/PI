package pl.polsl.project.catalogex.listElements.categoryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.ListItem

class CategoryListViewAdapter(private var layoutInflater: LayoutInflater, private var categoryList: ArrayList<ListItem>) : BaseAdapter() {

    private var isSelectionMode : Boolean = false
    private var selectedList: ArrayList<ListItem> = ArrayList()

    fun setIsSelectionMode(isSelectionMode:Boolean){
        this.isSelectionMode = isSelectionMode
    }

    fun getSelectedList(): ArrayList<ListItem>{
        return selectedList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: CategoryListViewHolder

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.element_checkbox_list, parent, false)
            vh = CategoryListViewHolder(view)
            view?.tag = vh

        } else {
            view = convertView
            vh = view.tag as CategoryListViewHolder
        }

        vh.tvTitle.text = categoryList[position].title

        if(isSelectionMode)
            vh.checkBox.visibility = View.VISIBLE
        else
            vh.checkBox.visibility = View.GONE

        vh.checkBox.setOnCheckedChangeListener {
            _, b ->
            if(b)
                selectedList.add(categoryList[position])
            else
                selectedList.remove(categoryList[position])

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