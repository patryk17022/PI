package pl.polsl.project.catalogex.database.entity


import android.arch.persistence.room.*

@Entity(tableName = "categories")
class CategoryEntity{

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"))
    @ColumnInfo(name = "category_id")
    var cat_id :Int? = -1

    @ForeignKey(entity = ElementEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("template_id"))
    @ColumnInfo(name = "template_id")
    var temp_id :Int? = -1

    var title: String = ""

    var isMain:Boolean = false

}