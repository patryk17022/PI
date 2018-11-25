package pl.polsl.project.catalogex.dialogs

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.dialog_sort.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.`interface`.ReturnDialogInterface
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.enums.SortMode

class SortDialog : DialogFragment() {

    var mode: SortMode = SortMode.NAME_ASC
    var rating : Boolean = false
    var activity: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerTemplate = ArrayList<String>()
        spinnerTemplate.add(getString(R.string.name))

        if(rating)
            spinnerTemplate.add(getString(R.string.rating_label))

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerTemplate)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sortBy.adapter = dataAdapter
        sortBy.setSelection(0)

        buttonAccept.setOnClickListener {
            view ->

            var elem = 0
            if(sortTypeDesc.isChecked)
                elem+= 2

            elem+= sortBy.selectedItemPosition

            mode = SortMode.values().get(elem)
            (activity as ReturnDialogInterface).doReturn()
            dismiss()

        }
    }

    fun sortTable(array : ArrayList<ListItem>){

        when(mode){
            SortMode.NAME_ASC->{
                array.sortBy({selectorTitle(it)})
            }

            SortMode.NAME_DESC->{
                array.sortByDescending({selectorTitle(it)})
            }

            SortMode.RATING_ASC->{
                array.sortBy({selectorRating(it as Element)})
            }

            SortMode.RATING_DESC->{
                array.sortByDescending({selectorRating(it as Element)})
            }
        }
    }

    fun selectorTitle(p: ListItem): String = p.title
    fun selectorRating(p: Element): Int = p.indicator
}