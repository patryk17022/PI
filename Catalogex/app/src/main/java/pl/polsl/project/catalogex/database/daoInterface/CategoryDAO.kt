package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.CategoryEntity

@Dao
interface CategoryDAO {

    @Query("SELECT * FROM categories")
    fun getAll(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: CategoryEntity) : Long

    @Delete
    fun delete(category: CategoryEntity)

}