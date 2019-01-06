package pl.polsl.project.catalogex.interfaces

import android.graphics.Bitmap

//Interfejs wykorzystywany do odbierania wybranego obrazu przez ekran wywołujący
interface ImageTakenInterface{
    fun imageHasBeenTaken(bitmap: Bitmap)
}