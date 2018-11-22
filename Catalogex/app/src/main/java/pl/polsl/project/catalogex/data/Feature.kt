package pl.polsl.project.catalogex.data

class Feature{
    var id: Int? = null
    var title: String = ""
    var detail: String = ""

    constructor(id: Int?, title: String, detail: String) {
        this.id = id
        this.title = title
        this.detail = detail
    }
}