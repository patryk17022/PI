package pl.polsl.project.catalogex.interfaces

//Interfejs wykrozystywany do obsługi w ekranie wywołującym okno dialogowe, przycisku zatwierdzającego
interface TextInputDialogInterface {
    fun doPositiveClick(tag:String, input:String, position: Int)
}