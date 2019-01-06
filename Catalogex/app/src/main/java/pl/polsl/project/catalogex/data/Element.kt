package pl.polsl.project.catalogex.data

import android.content.Context
import android.graphics.Bitmap
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.database.entity.ElementEntity
import java.io.ByteArrayOutputStream
import java.io.Serializable

//Klasa przedstawiająca przedmiot
@Suppress("ConvertToStringTemplate")
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

    //Metoda służąca do dodawania atrybutu
    fun addFeature(feature: Feature){
        list.add(feature)
    }

    //Metoda służąca do usuwania atrybutu
    fun removeFeature(index:Int){
        list.removeAt(index)
    }

    //Metoda służąca do kopiowania obiektu
    override fun copy():ListItem{
        val element = Element(title,category,indicator)

        for(i in 0 until list.size){
            element.list.add(Feature(list[i].title, list[i].detail))
        }

        element.image = image
        element.todo = todo

        return element
    }

    //Metoda służąca do przenoszenia wartości z innego elementu
     override fun insertValuesInto(elem: ListItem){
         val listElem = elem as Element
         listElem.title = title
         listElem.category = category
         listElem.indicator = indicator
         listElem.list = list
         listElem.image = image
         listElem.todo = todo
    }

    //Metoda zamieniająca Element na ElementEntity
    fun toElementEntity(parentId: Int? = null): ElementEntity {
        val elem = ElementEntity()
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

    //Metoda zamieniająca informacje zawarte w klasie na tekst
    fun exportToString(context: Context, asTemplate:Boolean=false) : String{

        var line = ""

        if(asTemplate){
            line+= context.getString(R.string.name_label) + ";"
            line+= context.getString(R.string.category_title) + ";"
            line+= context.getString(R.string.rating_label)

            for(elem in list){
                line+= ";" + elem.title
            }

        }else{
            line+= title+ ";"
            line+= category!!.title + ";"
            line+= indicator.toString()

            for(elem in list){
                line+= ";" + elem.detail
            }

        }

        return line +"\n"
    }
}