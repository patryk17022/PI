package pl.polsl.project.catalogex.data

import pl.polsl.project.catalogex.database.entity.FeatureEntity
import java.io.Serializable

class Feature: Serializable {

    var id:Int? = null

    var title: String
    var detail: String

    constructor(title: String, detail: String) {
        this.title = title
        this.detail = detail
    }

    fun toFeatureEntity(parentId: Int? = null): FeatureEntity {
        var feat = FeatureEntity()
        feat.id = this.id
        feat.elem_id = parentId
        feat.title = this.title
        feat.detail = this.detail
        return feat
    }
}