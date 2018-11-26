package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.CategoryEntity


@Dao
interface CategoryDAO {

    @Query("SELECT * FROM categories")
    fun getAll(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg categories: CategoryEntity)

    @Delete
    fun delete(category: CategoryEntity)

}