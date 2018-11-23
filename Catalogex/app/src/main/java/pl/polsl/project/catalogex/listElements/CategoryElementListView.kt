package pl.polsl.project.catalogex.listElements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.display.ShowElementInformationScreen
import pl.polsl.project.catalogex.display.ShowMainScreen

interface CategoryElementInterface{
    fun setListElement(postion:Int)
}

private class CategoryElementListViewHolder(view: View?) {
    val tvTitle: TextView
    val imButton: ImageButton
    val button: Button

    init {
        this.tvTitle = view?.findViewById(R.id.informationText) as TextView
        this.imButton = view?.findViewById(R.id.dropDownListCategory) as ImageButton
        this.button = view?.findViewById(R.id.elementInfoButton) as Button
    }
}

class  CategoryElementListViewAdapter : BaseAdapter {

    private var categoryList = ArrayList<Element>()
    private var context: Context? = null
    private var layoutInflater : LayoutInflater? = null
    private var activity : Activity? = null

    constructor(context: Context, categoryList: ArrayList<Element>, layoutInflater: LayoutInflater, activity: Activity) : super() {
        this.categoryList = categoryList
        this.context = context
        this.layoutInflater = layoutInflater
        this.activity = activity
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
            (activity as CategoryElementInterface).setListElement(position)
            popup.setOnMenuItemClickListener(activity as PopupMenu.OnMenuItemClickListener)
            popup.show()
            true
        }

        vh.button.setOnClickListener{ view ->
            val intent = Intent(context, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = categoryList.get(position)
            activity!!.startActivity(intent)
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
