package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.ElementEntity


@Dao
interface ElementDAO {

    @Query("SELECT * FROM elements")
    fun getAll(): List<ElementEntity>

    @Query("SELECT * FROM elements WHERE category_id IS NOT NULL AND todo = 0")
    fun getElementsAll(): List<ElementEntity>

    @Query("SELECT * FROM elements WHERE category_id IS NOT NULL AND todo = 1")
    fun getToDoAll(): List<ElementEntity>

    @Query("SELECT * FROM elements WHERE category_id IS NULL AND todo = 0")
    fun getTemplateAll(): List<ElementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elements: ElementEntity)

    @Delete
    fun delete(element: ElementEntity)
}