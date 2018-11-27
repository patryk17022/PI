package pl.polsl.project.catalogex.data

import java.io.Serializable

open class ListItem: Serializable {

    var id:Int? = null

    var title: String

    constructor(title: String) {
        this.title = title
    }

    open fun copy(): ListItem {
        var listItem = ListItem(title)
        return listItem
    }

    open fun insertValuesInto(elem: ListItem) {
        elem.title = title
    }
}