package pl.polsl.project.catalogex.data

import pl.polsl.project.catalogex.database.entity.CategoryEntity
import java.io.Serializable

//Klasa przedstawiająca kategorię
class Category:ListItem, Serializable{

    var list :ArrayList<ListItem> = arrayListOf()
    var template: Element? = null

    constructor() : super("")

    constructor(title: String, template: Element?) : super(title)
    {
        this.title = title
        this.template = template
    }

    //Metoda służąca do kopiowania obiektu
    override fun copy():ListItem{
        val category = Category(title,template)

        for(i in 0 until list.size){
            category.list.add(list[i].copy())
        }

        return category
    }

    //Metoda służąca do przenoszenia wartości z innego elementu
    override fun insertValuesInto(elem: ListItem){
        val listElem = elem as Category
        listElem.title = title
        listElem.list = list
        listElem.template = template
    }

    //Metoda zamieniająca Category na CategoryEntity
    fun toCategoryEntity(parentId: Int? = null, templateId: Int? = null, isMain: Boolean = false): CategoryEntity {
        val elem = CategoryEntity()
        elem.id = this.id
        elem.cat_id = parentId
        elem.temp_id = templateId
        elem.title = this.title

        if(isMain)
            elem.cat_id = null

        return elem
    }
}