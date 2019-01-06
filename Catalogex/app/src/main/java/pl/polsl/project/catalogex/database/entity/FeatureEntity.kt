package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.data.Feature

//Klasa reprezentująca wiersz z tabeli features
@Suppress("PropertyName")
@Entity(tableName = "features")
class FeatureEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ForeignKey(entity = ElementEntity::class,
            parentColumns = ["id"],
            childColumns = ["element_id"])
    @ColumnInfo(name = "element_id")
    var elem_id :Int? = null

    var title: String = ""
    var detail: String = ""

    //Metoda zamieniająca FeatureEntity na Feature
    fun toFeature() : Feature{
        val elem = Feature(this.title,this.detail)
        elem.id = this.id
        return elem
    }

}