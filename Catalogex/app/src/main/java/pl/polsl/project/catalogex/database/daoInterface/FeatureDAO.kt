package pl.polsl.project.catalogex.database.daoInterface

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.entity.FeatureEntity

@Dao
interface FeatureDAO {

    @Query("SELECT * FROM features")
    fun getAll(): List<FeatureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: FeatureEntity) : Long

    @Delete
    fun delete(feature: FeatureEntity)
}