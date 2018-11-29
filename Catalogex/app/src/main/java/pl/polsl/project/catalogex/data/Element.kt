package pl.polsl.project.catalogex.data

import android.graphics.Bitmap
import pl.polsl.project.catalogex.database.entity.ElementEntity
import java.io.ByteArrayOutputStream
import java.io.Serializable

class Element:ListItem, Serializable {

    var list :ArrayList<Feature> = arrayListOf()
    var category : Category? = null
    var image : Bitmap? = null
    var indicator: Int = 0
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

    override fun copy():ListItem{
        var element = Element(title,category,indicator)

        for(i in 0 until list.size){
            element.list.add(Feature(list.get(i).title, list.get(i).detail))
        }

        element.image = image
        element.todo = todo

        return element
    }

     override fun insertValuesInto(elem: ListItem){
         var listElem = elem as Element
         listElem.title = title
         listElem.category = category
         listElem.indicator = indicator
         listElem.list = list
         listElem.image = image
         listElem.todo = todo
    }

    fun toElementEntity(parentId: Int? = null): ElementEntity {
        var elem = ElementEntity()
        elem.id = this.id

        if(this.image != null) {
            val stream = ByteArrayOutputStream()
            this.image!!.compress(Bitmap.CompressFormat.PNG, 90, stream)
            elem.image = stream.toByteArray()
        } else {
            this.image = null
        }

        elem.cat_id = parentId
        elem.title= this.title
        elem.indicator = this.indicator
        elem.todo = this.todo
        return elem
    }
}