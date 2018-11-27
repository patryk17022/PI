package pl.polsl.project.catalogex.database.entity

import android.arch.persistence.room.*
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.ListItem
import javax.xml.transform.Templates

@Entity(tableName = "categories")
class CategoryEntity{

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"))
    @ColumnInfo(name = "category_id")
    var cat_id :Int? = null

    @ForeignKey(entity = ElementEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("template_id"))
    @ColumnInfo(name = "template_id")
    var temp_id :Int? = null

    var title: String = ""

    var isMain:Boolean = false

    fun toCategory(childList:ArrayList<ListItem>? = null, template:Element? = null): Category{
        var elem = Category()
        elem.id = this.id
        elem.title = this.title
        elem.template = template

        if(childList == null)
            elem.list = arrayListOf()
        else
            elem.list = childList

        return elem
    }

}