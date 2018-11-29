package pl.polsl.project.catalogex.dialogs

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.dialog_filter.*
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.interfaces.ReturnDialogInterface
import pl.polsl.project.catalogex.data.ListItem

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class FilterDialog : DialogFragment() {

    private var activity: Activity? = null
    private var category: Category? = null
    private var titleActual: String? = null
    private var valueActual: String? = null
    private var filterList: ArrayList<FilterElement> = arrayListOf()

    class FilterElement {
        var title: String = ""
        var values: ArrayList<String> = arrayListOf()
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    fun setCategory(cat: Category) {
        category = cat
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_filter, container)
    }

    fun prepareList(categ: Category) {

        for (cat in categ.list) {
            if (cat is Category) {
                prepareList(cat)
            } else {
                for (feat in (cat as Element).list) {

                    var isInList = false
                    for (i in filterList) {
                        if (i.title == feat.title) {
                            isInList = true

                            if (!i.values.contains(feat.detail)) {
                                i.values.add(feat.detail)
                            }

                            break
                        }
                    }

                    if (isInList == false) {
                        var felem = FilterElement()
                        felem.title = feat.title
                        felem.values.add(feat.detail)
                        filterList.add(felem)
                    }


                }
            }
        }
    }

    fun initializeSpinnerTitle() {

        var index = 0

        val spinnerTemplateTitle = ArrayList<String>()
        for (elem in filterList) {
            spinnerTemplateTitle.add(elem.title)

            if(titleActual != null && elem.title == titleActual){
                index = filterList.indexOf(elem)
            }
        }

        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerTemplateTitle)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        filterBy.adapter = dataAdapter
        filterBy.setSelection(index)
    }

    fun initializeSpinnerValue() {

        var index = 0

        val spinnerTemplateValue = ArrayList<String>()
        for (value in filterList.get(filterBy.selectedItemPosition).values) {
            spinnerTemplateValue.add(value)
            if(valueActual != null && value == valueActual){
                index = filterList.get(filterBy.selectedItemPosition).values.indexOf(value)
            }
        }

        val dataAdapter2 = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerTemplateValue)
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        valueBy.adapter = dataAdapter2
        valueBy.setSelection(index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterList.clear()

        var filtr = FilterElement()
        filtr.title = getString(R.string.none)
        filtr.values.add(getString(R.string.none))
        filterList.add(filtr)

        filtr = FilterElement()
        filtr.title = getString(R.string.rating_label)
        for (i in 0 until 11) {
            filtr.values.add(i.toString())
        }
        filterList.add(filtr)

        prepareList(category!!)

        initializeSpinnerTitle()
        initializeSpinnerValue()

        filterBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                initializeSpinnerValue()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        buttonAccept.setOnClickListener { viewL ->

            var elem = filterList.get(filterBy.selectedItemPosition)
            titleActual = elem.title
            valueActual = elem.values.get(valueBy.selectedItemPosition)

            (activity as ReturnDialogInterface).doReturn()

            dismiss()

        }

        buttonCancle.setOnClickListener {
            titleActual = null
            valueActual = null
            dismiss()
        }
    }

    fun filter(array: ArrayList<ListItem>) {

        if(titleActual!= null) {
            var todel = ArrayList<ListItem>()

            when (titleActual) {
                filterList[0].title-> return

                filterList[1].title -> for (elem in array) {
                    if (!filterCategories(arrayListOf(elem), valueActual!!.toInt()))
                        todel.add(elem)
                }

                else -> for (elem in array) {
                    if (!filterCategories(arrayListOf(elem), null))
                        todel.add(elem)
                }
            }

            array.removeAll(todel)
        }
    }

    private fun filterCategories(array: ArrayList<ListItem>, rating: Int?) :Boolean{

        for (elem in array) {

            if(elem is Category)
            {
                if(filterCategories(elem.list,rating))
                    return true

            }else if(elem is Element)
            {
                if(rating != null) {
                    if (elem.indicator == rating) {
                        return true
                    }
                }else{

                    for(feat in elem.list)
                    {
                        if(feat.title == titleActual)
                        {
                            if(feat.detail == valueActual)
                            {
                                return true
                            }
                        }
                    }

                }
            }
        }
        return false

    }
}