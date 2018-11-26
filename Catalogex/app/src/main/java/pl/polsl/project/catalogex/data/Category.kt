package pl.polsl.project.catalogex.data

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

    override fun insertValuesFrom(elem: ListItem){
        val listElem = elem as Category
        listElem.title = title
        listElem.list = list
        listElem.template = template
    }
}