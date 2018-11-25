package pl.polsl.project.catalogex.listElements.elementDetails

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.`interface`.ElementDetailsInterface
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.enums.DetailListMode

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class  ElementDetailListViewAdapter(private var detailList: ArrayList<Feature>, layoutInflater: LayoutInflater, activity: Activity, mode: DetailListMode) : BaseAdapter() {

    private var layoutInflater : LayoutInflater? = layoutInflater
    private var mode : DetailListMode? = mode
    private var activity :Activity? = activity

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

        vh.imEdit.setOnClickListener { viewL ->
            (activity as ElementDetailsInterface).onEditButton(position)
        }

        vh.imDelete.setOnClickListener { viewL ->
            (activity as ElementDetailsInterface).onDeleteButton(position)
        }

        vh.imAdd.setOnClickListener { viewL ->
            (activity as ElementDetailsInterface).onAddButton(position)
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
