package pl.polsl.project.catalogex.data

import java.io.Serializable

class Feature: Serializable {

    var id:Int? = null

    var title: String
    var detail: String

    constructor(title: String, detail: String) {
        this.title = title
        this.detail = detail
    }
}