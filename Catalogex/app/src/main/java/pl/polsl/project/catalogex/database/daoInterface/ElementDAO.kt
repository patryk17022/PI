package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.ElementEntity

//Interfejs pozwalający na komunkację z bazą danych dla Element
@Dao
interface ElementDAO {

    //Metoda pozwalająca na pobieranie elementów z bazy
    @Query("SELECT * FROM elements")
    fun getAll(): List<ElementEntity>

    //Metoda pozwalająca na pobieranie elementów z bazy, które nie znajdują się w liście TO DO
    @Query("SELECT * FROM elements WHERE category_id IS NOT NULL AND todo = 0")
    fun getElementsAll(): List<ElementEntity>

    //Metoda pozwalająca na pobieranie elementów z bazy, które znajdują się w liście TO DO
    @Query("SELECT * FROM elements WHERE category_id IS NOT NULL AND todo = 1")
    fun getToDoAll(): List<ElementEntity>

    //Metoda pozwalająca na pobieranie elementów z bazy, które są wzorami
    @Query("SELECT * FROM elements WHERE category_id IS NULL AND todo = 0")
    fun getTemplateAll(): List<ElementEntity>

    //Metoda pozwalająca na dodawanie elementów z bazy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: ElementEntity) : Long

    //Metoda pozwalająca na usuwanie elementów z bazy
    @Delete
    fun delete(element: ElementEntity)
}