package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*

@Entity(tableName = "features")
class FeatureEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ForeignKey(entity = ElementEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("element_id"))
    @ColumnInfo(name = "element_id")
    var elem_id :Int = -1

    var title: String = ""
    var detail: String = ""

}