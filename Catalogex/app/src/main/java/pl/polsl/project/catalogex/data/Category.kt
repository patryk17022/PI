package pl.polsl.project.catalogex.data

import pl.polsl.project.catalogex.database.entity.CategoryEntity
import java.io.Serializable

class Category:ListItem, Serializable{

    var list :ArrayList<ListItem> = arrayListOf()
    var template: Element? = null

    constructor() : super("")

    constructor(title: String, template: Element?) : super(title)
    {
        this.title = title
        this.template = template
    }

    override fun copy():ListItem{
        var categ = Category(title,template)

        for(i in 0 until list.size){
            categ.list.add(list.get(i).copy())
        }

        return categ
    }

    override fun insertValuesInto(elem: ListItem){
        val listElem = elem as Category
        listElem.title = title
        listElem.list = list
        listElem.template = template
    }

    fun toCategoryEntity(parentId: Int? = null, templateId: Int? = null, isMain: Boolean = false): CategoryEntity {
        var elem = CategoryEntity()
        elem.id = this.id
        elem.cat_id = parentId
        elem.temp_id = templateId
        elem.title = this.title

        if(isMain)
            elem.cat_id = null

        return elem
    }
}