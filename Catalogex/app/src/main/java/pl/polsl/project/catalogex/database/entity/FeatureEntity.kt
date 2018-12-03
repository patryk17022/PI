package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.data.Feature

@Entity(tableName = "features")
class FeatureEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ForeignKey(entity = ElementEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("element_id"))
    @ColumnInfo(name = "element_id")
    var elem_id :Int? = null

    var title: String = ""
    var detail: String = ""

    fun toFeature() : Feature{
        var elem = Feature(this.title,this.detail)
        elem.id = this.id
        return elem
    }

}