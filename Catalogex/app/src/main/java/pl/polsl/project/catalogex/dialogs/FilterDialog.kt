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

//Klasa odpowiedzialna za obsługę okna dialogowego, wykorzystywanego do ustawienia filtrowania
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

    //Metoda jest wywoływana podczas tworzenia widoku
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_filter, container)
    }

    //Metoda przygotowująca elementy dostępne w liście
    private fun prepareList(category: Category) {

        for (cat in category.list) {
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

                    if (!isInList) {
                        val featureElem = FilterElement()
                        featureElem.title = feat.title
                        featureElem.values.add(feat.detail)
                        filterList.add(featureElem)
                    }

                }
            }
        }
    }

    //Metoda inicjalizująca wartości spinnera
    private fun initializeSpinnerTitle() {

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

    //Metoda inicjalizująca wartości spinnera
    fun initializeSpinnerValue() {

        var index = 0

        val spinnerTemplateValue = ArrayList<String>()
        for (value in filterList[filterBy.selectedItemPosition].values) {
            spinnerTemplateValue.add(value)
            if(valueActual != null && value == valueActual){
                index = filterList[filterBy.selectedItemPosition].values.indexOf(value)
            }
        }

        val dataAdapter2 = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerTemplateValue)
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        valueBy.adapter = dataAdapter2
        valueBy.setSelection(index)
    }

    //Metoda jest wywoływana po tworzeniu widoku
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterList.clear()

        var filter = FilterElement()
        filter.title = getString(R.string.none)
        filter.values.add(getString(R.string.none))
        filterList.add(filter)

        filter = FilterElement()
        filter.title = getString(R.string.rating_label)
        for (i in 0 until 11) {
            filter.values.add(i.toString())
        }
        filterList.add(filter)

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

            val elem = filterList[filterBy.selectedItemPosition]
            titleActual = elem.title
            valueActual = elem.values[valueBy.selectedItemPosition]

            (activity as ReturnDialogInterface).doReturn()

            dismiss()

        }

        buttonCancel.setOnClickListener {
            titleActual = null
            valueActual = null
            dismiss()
        }
    }

    //Metoda wykorzystywana do filtrowania
    fun filter(array: ArrayList<ListItem>) {

        if(titleActual!= null) {
            val toDel = ArrayList<ListItem>()

            when (titleActual) {
                filterList[0].title-> return

                filterList[1].title -> for (elem in array) {
                    if (!filterCategories(arrayListOf(elem), valueActual!!.toInt()))
                        toDel.add(elem)
                }

                else -> for (elem in array) {
                    if (!filterCategories(arrayListOf(elem), null))
                        toDel.add(elem)
                }
            }

            array.removeAll(toDel)
        }
    }

    //Metoda filtrująca categorie, znajduje czy istnieje w niej szukany element
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