package pl.polsl.project.catalogex.listElements.TodoElement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.`interface`.TodoElementInterface
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.display.ShowCategoryListScreen
import pl.polsl.project.catalogex.display.ShowElementInformationScreen
import pl.polsl.project.catalogex.display.ShowMainScreen

class  TodoElementListViewAdapter : BaseAdapter {

    private var categoryList = ArrayList<Element>()
    private var context: Context? = null
    private var layoutInflater : LayoutInflater? = null
    private var activity : Activity? = null
    var selectedList: ArrayList<ListItem> = ArrayList()

    constructor(context: Context, categoryList: ArrayList<Element>, layoutInflater: LayoutInflater, activity: Activity) : super() {
        this.categoryList = categoryList
        this.context = context
        this.layoutInflater = layoutInflater
        this.activity = activity
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: TodoElementListViewHolder

        if (convertView == null) {
            view = layoutInflater?.inflate(R.layout.element_category_list, parent, false)
            vh = TodoElementListViewHolder(view)
            view?.tag = vh

        } else {
            view = convertView
            vh = view.tag as TodoElementListViewHolder
        }

        vh.tvTitle.text = categoryList[position].title

        vh.imButton.setOnClickListener { view ->
            val popup = PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.element_menu_todo_popup, popup.menu)
            (activity as TodoElementInterface).setListElement(position)
            popup.setOnMenuItemClickListener(activity as PopupMenu.OnMenuItemClickListener)
            popup.show()
            true
        }

        vh.button.setOnClickListener{ view ->
            val intent = Intent(context, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = categoryList.get(position)
            activity!!.startActivity(intent)
        }

        if(ShowCategoryListScreen.isSelectionMode){
            vh.check.visibility = View.VISIBLE
            vh.imButton.visibility = View.GONE
        }else{
            vh.check.visibility = View.GONE
            vh.imButton.visibility = View.VISIBLE
        }

        vh.check.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                selectedList.add(categoryList.get(position))
            } else{
                selectedList.remove(categoryList.get(position))
            }
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
