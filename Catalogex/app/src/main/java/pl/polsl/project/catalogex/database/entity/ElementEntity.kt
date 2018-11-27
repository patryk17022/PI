package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import android.arch.persistence.room.ColumnInfo
import android.graphics.BitmapFactory


@Entity(tableName = "elements")
class ElementEntity{

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    @ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"))
    @ColumnInfo(name = "category_id")
    var cat_id : Int? = null

    var title: String = ""
    var indicator: Int = 0
    var todo : Boolean = false

    fun toElement(parentCategory: Category? = null ,childFeatureList: ArrayList<Feature>? = null) : Element{
        var elem = Element()
        elem.id = this.id
        elem.title = this.title
        elem.category = parentCategory

        if(childFeatureList != null)
            elem.list = childFeatureList
        else
            elem.list = arrayListOf()

        if(this.image != null){
            elem.image = BitmapFactory.decodeByteArray(this.image, 0, this.image!!.size)
        }else{
            elem.image = null
        }

        elem.indicator = this.indicator
        elem.todo = this.todo
        return elem
    }

}