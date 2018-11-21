package pl.polsl.project.catalogex.data

class Category:ListItem{
    var list :ArrayList<ListItem> = arrayListOf()
    var template: Element? = null

    constructor() : super("")

    constructor(title: String, template: Element) : super(title)
    {
        this.title = title
        this.template = template
    }
}