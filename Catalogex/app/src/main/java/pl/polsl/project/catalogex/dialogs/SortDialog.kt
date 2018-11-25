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

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class SortDialog : DialogFragment() {

    private var mode: SortMode = SortMode.NAME_ASC
    private var element : Element? = null
    private var activity: Activity? = null
    private var whichFeatureSort: Int = 0

    fun setActivity(activity: Activity){
        this.activity=activity
    }

    fun setElement(element : Element?){
        this.element=element
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerTemplate = ArrayList<String>()
        spinnerTemplate.add(getString(R.string.name))

        if(element != null) {

            spinnerTemplate.add(getString(R.string.rating_label))

            for (i in element!!.list)
                spinnerTemplate.add(i.title)

        }

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerTemplate)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sortBy.adapter = dataAdapter
        sortBy.setSelection(0)

        buttonAccept.setOnClickListener {
            viewL ->

            var elem = 0
            elem+= sortBy.selectedItemPosition

            whichFeatureSort = sortBy.selectedItemPosition - 2

            if(sortBy.selectedItemPosition >= 2)
                elem=4

            if(sortTypeDesc.isChecked)
                elem+= 2

            mode = SortMode.values()[elem]
            (activity as ReturnDialogInterface).doReturn()

            dismiss()

        }
    }

    fun selectorTitle(p: ListItem): String = p.title
    fun selectorRating(p: Element): Int = p.indicator
    fun selectorFeature(p: Element): String = p.list.get(whichFeatureSort).detail

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

            SortMode.FEATURE_ASC->{
                array.sortBy({selectorFeature(it as Element)})
            }

            SortMode.FEATURE_DESC->{
                array.sortByDescending({selectorFeature(it as Element)})
            }
            else -> {}
        }
    }
}