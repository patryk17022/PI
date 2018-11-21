package pl.polsl.project.catalogex.data

import android.graphics.Bitmap

class Element:ListItem{
    var list :ArrayList<Feature> = arrayListOf()
    var category : Category? = null
    var indicator: Int = 0
    var image : Bitmap? = null

    constructor() : super("")

    constructor(title: String, category: Category, indicator:Int) : super(title)
    {
        this.title = title
        this.category = category
        this.indicator = indicator
    }

    fun addFeature(feature: Feature){
        list.add(feature)
    }

    fun removeFeature(index:Int){
        list.removeAt(index)
    }
}