package pl.polsl.project.catalogex.data

import java.io.Serializable

open class ListItem(var title: String) : Serializable {

    var id:Int? = null

    open fun copy(): ListItem {
        return ListItem(title)
    }

    open fun insertValuesInto(elem: ListItem) {
        elem.title = title
    }
}