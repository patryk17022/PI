package pl.polsl.project.catalogex.listElements.todoElement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.display.ShowElementInformationScreen
import pl.polsl.project.catalogex.display.ShowMainScreen
import pl.polsl.project.catalogex.display.ShowTodoScreen

//Klasa obsługująca wyświetlane elementy w liście TO DO
@SuppressLint("SetTextI18n")
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class  TodoElementListViewAdapter(private var categoryList: ArrayList<Element>, layoutInflater: LayoutInflater, activity: Activity) : BaseAdapter() {

    private var layoutInflater : LayoutInflater? = layoutInflater
    private var activity : Activity? = activity
    private var isSelectionMode : Boolean = false
    private var selectedList: ArrayList<ListItem> = ArrayList()

    fun setIsSelectionMode(isSelectionMode:Boolean){
        this.isSelectionMode = isSelectionMode
    }

    fun getSelectedList():ArrayList<ListItem>{
        return selectedList
    }

    //Funkcja wywoływana dla poszczególnych elementów listy w celu ustawienia ich wartości
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
        vh.info.text = activity!!.getString(R.string.from) + " " + categoryList[position].category!!.title

        vh.imButton.setOnClickListener { viewL ->
            val popup = PopupMenu(activity, viewL)
            popup.menuInflater.inflate(R.menu.menu_todo_popup, popup.menu)
            (activity as ShowTodoScreen).setListElement(position)
            popup.setOnMenuItemClickListener(activity as PopupMenu.OnMenuItemClickListener)
            popup.show()
        }

        vh.button.setOnClickListener{ viewL ->
            val intent = Intent(activity, ShowElementInformationScreen::class.java)
            ShowMainScreen.actualElement = categoryList[position]
            activity!!.startActivity(intent)
        }

        if(isSelectionMode){
            vh.check.visibility = View.VISIBLE
            vh.imButton.visibility = View.GONE
        }else{
            vh.check.visibility = View.GONE
            vh.imButton.visibility = View.VISIBLE
        }

        vh.check.setOnCheckedChangeListener {
            _, b ->

            if(b)
                selectedList.add(categoryList[position])
             else
                selectedList.remove(categoryList[position])

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
