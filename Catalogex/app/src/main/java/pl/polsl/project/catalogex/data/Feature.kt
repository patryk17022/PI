package pl.polsl.project.catalogex.data
import java.io.Serializable

class Feature: Serializable {
    var id: Int
    var title: String
    var detail: String

    constructor(id: Int, title: String, detail: String) {
        this.id = id
        this.title = title
        this.detail = detail
    }
}