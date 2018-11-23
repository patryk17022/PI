package pl.polsl.project.catalogex.data

open class ListItem {
    var title: String = ""

    constructor(title: String){
        this.title = title
    }

    open fun copy():ListItem{
        var listItem = ListItem(title)
        return listItem
    }

    open fun insertValuesFrom(elem: ListItem) {
        elem.title = title
    }
}