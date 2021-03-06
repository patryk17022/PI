package pl.polsl.project.catalogex.data

import pl.polsl.project.catalogex.database.entity.FeatureEntity
import java.io.Serializable

//Klasa przedstawiająca atrybut przedmiotu
class Feature(var title: String, var detail: String) : Serializable {

    var id:Int? = null

    //Metoda zamieniająca Feature na FeatureEntity
    fun toFeatureEntity(parentId: Int? = null): FeatureEntity {
        val feat = FeatureEntity()
        feat.id = this.id
        feat.elem_id = parentId
        feat.title = this.title
        feat.detail = this.detail
        return feat
    }
}