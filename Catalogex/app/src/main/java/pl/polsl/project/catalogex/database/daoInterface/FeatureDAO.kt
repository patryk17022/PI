package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.FeatureEntity

//Interfejs pozwalający na komunkację z bazą danych dla Feature
@Dao
interface FeatureDAO {

    //Metoda pozwalająca na pobieranie elementów z bazy
    @Query("SELECT * FROM features")
    fun getAll(): List<FeatureEntity>

    //Metoda pozwalająca na dodawanie elementów z bazy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: FeatureEntity) : Long

    //Metoda pozwalająca na usuwanie elementów z bazy
    @Delete
    fun delete(feature: FeatureEntity)
}