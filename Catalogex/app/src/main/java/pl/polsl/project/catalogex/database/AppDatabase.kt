package pl.polsl.project.catalogex.database

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.database.daoInterface.CategoryDAO
import pl.polsl.project.catalogex.database.daoInterface.ElementDAO
import pl.polsl.project.catalogex.database.daoInterface.FeatureDAO
import pl.polsl.project.catalogex.database.entity.CategoryEntity
import pl.polsl.project.catalogex.database.entity.ElementEntity
import pl.polsl.project.catalogex.database.entity.FeatureEntity

@Database(entities = arrayOf(CategoryEntity::class,ElementEntity::class,FeatureEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun elementDAO(): ElementDAO
    abstract fun featureDAO(): FeatureDAO

}