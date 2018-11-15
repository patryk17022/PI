package pl.polsl.project.catalogex.listElements

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import pl.polsl.project.catalogex.R

class CategoryElementListView {

    var id: Int? = null
    var title: String? = null

    constructor(id: Int, title: String) {
        this.id = id
        this.title = title
    }
}


private class CategoryElementListViewHolder(view: View?) {
    val tvTitle: TextView
    val imButton: ImageButton

    init {
        this.tvTitle = view?.findViewById(R.id.informationText) as TextView
        this.imButton = view?.findViewById(R.id.dropDownListCategory) as ImageButton
    }
}

class  CategoryElementListViewAdapter : BaseAdapter {

    private var categoryList = ArrayList<CategoryElementListView>()
    private var context: Context? = null
    private var layoutInflater : LayoutInflater? = null

    constructor(context: Context, categoryList: ArrayList<CategoryElementListView>, layoutInflater: LayoutInflater) : super() {
        this.categoryList = categoryList
        this.context = context
        this.layoutInflater = layoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: CategoryElementListViewHolder

        if (convertView == null) {
            view = layoutInflater?.inflate(R.layout.element_category_list, parent, false)
            vh = CategoryElementListViewHolder(view)
            view?.tag = vh

        } else {
            view = convertView
            vh = view.tag as CategoryElementListViewHolder
        }

        vh.tvTitle.text = categoryList[position].title

        vh.imButton.setOnClickListener { view ->
            val popup = PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.element_menu_todo_popup, popup.menu)

            popup.show()
            true
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categoryList.size
    }
}
