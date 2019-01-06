package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.CategoryEntity

//Interfejs pozwalający na komunkację z bazą danych dla Category
@Dao
interface CategoryDAO {

    //Metoda pozwalająca na pobieranie elementów z bazy
    @Query("SELECT * FROM categories")
    fun getAll(): List<CategoryEntity>

    //Metoda pozwalająca na dodawanie elementów z bazy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: CategoryEntity) : Long

    //Metoda pozwalająca na usuwanie elementów z bazy
    @Delete
    fun delete(category: CategoryEntity)

}