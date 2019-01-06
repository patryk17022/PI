package pl.polsl.project.catalogex.data

import java.io.Serializable

//Klasa bazowa dla struktury danych
open class ListItem(var title: String) : Serializable {

    var id:Int? = null

    //Metoda służąca do kopiowania obiektu
    open fun copy(): ListItem {
        return ListItem(title)
    }

    //Metoda służąca do przenoszenia wartości z innego elementu
    open fun insertValuesInto(elem: ListItem) {
        elem.title = title
    }
}