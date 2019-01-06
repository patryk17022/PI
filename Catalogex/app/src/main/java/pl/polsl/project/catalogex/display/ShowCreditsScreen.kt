package pl.polsl.project.catalogex.display

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.polsl.project.catalogex.R

//Klasa odpowiedzialna za obsługę ekranu wyświetlającego informacje o autorze
class ShowCreditsScreen : AppCompatActivity() {

    //Metoda wywoływana w momencie tworzenia instancji klasy podczas uruchomienia ekranu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits_screen)
    }
}
