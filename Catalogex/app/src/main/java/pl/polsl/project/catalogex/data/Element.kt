package pl.polsl.project.catalogex.data

import android.graphics.Bitmap

class Element:ListItem{
    var list :ArrayList<Feature> = arrayListOf()
    var category : Category? = null
    var indicator: Int = 0
    var image : Bitmap? = null
    var todo : Boolean = false

    constructor() : super("")

    constructor(title: String, category: Category?, indicator:Int) : super(title)
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

    fun copy():Element{
        var element = Element(title,category,indicator)
        element.image = image

        for(i in 0 until list.size){
            element.list.add(Feature(list.get(i).id, list.get(i).title, list.get(i).detail))
        }

        return element
    }
}