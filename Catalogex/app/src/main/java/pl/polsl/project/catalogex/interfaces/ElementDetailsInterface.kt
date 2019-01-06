package pl.polsl.project.catalogex.interfaces

//Interfejs obsługujący operacje wykonywane na liście atrybutów w przedmiocie
interface ElementDetailsInterface {
    fun onEditButton(position: Int){}
    fun onDeleteButton(position: Int){}
    fun onAddButton(position: Int){}
}