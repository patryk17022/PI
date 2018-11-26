package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*
import android.graphics.Bitmap

@Entity(tableName = "elements")
class ElementEntity{

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @Embedded
    var image : Bitmap? = null

    @ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"))
    @ColumnInfo(name = "category_id")
    var cat_id : Int? = null

    var title: String = ""
    var indicator: Int = 0
    var todo : Boolean = false

}