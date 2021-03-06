package pl.polsl.project.catalogex.enums

//Enumerator dla ekranu przedstawiającego informację o przedmiocie w celu wybrania odpowiednich elementów listy
@Suppress("unused")
enum class DetailListMode(val mode: Int){
    NONE_BUTTON(0),
    EDIT_BUTTON(1),
    DELETE_BUTTON(2),
    ADD_BUTTON(3),
    EDIT_DELETE_BUTTON(4)
}