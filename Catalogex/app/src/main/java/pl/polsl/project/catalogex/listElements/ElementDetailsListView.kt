package pl.polsl.project.catalogex.listElements

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Feature

interface ElementDetailsInterface {
    fun onEditButton(position: Int){}
    fun onDeleteButton(position: Int){}
    fun onAddButton(position: Int){}
}

enum class DetailListMode(val mode: Int){
    NONE_BUTTON(0),
    EDIT_BUTTON(1),
    DELETE_BUTTON(2),
    ADD_BUTTON(3),
    EDIT_DELETE_BUTTON(4)
}

private class ElementDetailListViewHolder(view: View?) {
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

class  ElementDetailListViewAdapter : BaseAdapter {

    private var detailList = ArrayList<Feature>()
    private var context: Context? = null
    private var layoutInflater : LayoutInflater? = null
    private var mode :DetailListMode? = null
    private var activity :Activity? = null

    constructor(context: Context, detailList: ArrayList<Feature>, layoutInflater: LayoutInflater,activity :Activity, mode:DetailListMode) : super() {
        this.detailList = detailList
        this.context = context
        this.layoutInflater = layoutInflater
        this.mode = mode
        this.activity = activity
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val vh: ElementDetailListViewHolder

        if (convertView == null) {
            view = layoutInflater?.inflate(R.layout.element_details_list, parent, false)
            vh = ElementDetailListViewHolder(view)
            view?.tag = vh

        } else {
            view = convertView
            vh = view.tag as ElementDetailListViewHolder
        }

        vh.tvTitle.text = detailList[position].title
        vh.tvValue.text = detailList[position].detail

        when(mode){
            DetailListMode.NONE_BUTTON -> {
                vh.imDelete.visibility = View.GONE
                vh.imEdit.visibility = View.GONE
                vh.imAdd.visibility = View.GONE
            }

            DetailListMode.DELETE_BUTTON -> {
                vh.imEdit.visibility = View.GONE
                vh.imAdd.visibility = View.GONE
            }

            DetailListMode.EDIT_BUTTON -> {
                vh.imDelete.visibility = View.GONE
                vh.imAdd.visibility = View.GONE
            }
            DetailListMode.ADD_BUTTON -> {
                vh.imDelete.visibility = View.GONE
                vh.imEdit.visibility = View.GONE
            }
            DetailListMode.EDIT_DELETE_BUTTON -> {
                vh.imAdd.visibility = View.GONE
            }
        }

        vh.imEdit.setOnClickListener { view ->
            (activity as ElementDetailsInterface).onEditButton(position)
            true
        }

        vh.imDelete.setOnClickListener { view ->
            (activity as ElementDetailsInterface).onDeleteButton(position)
            true
        }

        vh.imAdd.setOnClickListener { view ->
            (activity as ElementDetailsInterface).onAddButton(position)

            true
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return detailList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return detailList.size
    }
}
