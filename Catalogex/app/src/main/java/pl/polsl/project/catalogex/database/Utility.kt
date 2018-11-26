package pl.polsl.project.catalogex.database

import android.app.Application
import android.arch.persistence.room.*
import android.arch.persistence.room.migration.Migration
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.entity.CategoryEntity
import pl.polsl.project.catalogex.database.entity.ElementEntity
import pl.polsl.project.catalogex.database.entity.FeatureEntity

class Utility : Application() {

    override fun onCreate() {
        super.onCreate()
        db =  Room.databaseBuilder(applicationContext, AppDatabase::class.java, "catalogex_db").allowMainThreadQueries().build()
    }


    companion object {

        var db: AppDatabase? = null


        fun getMainCategory(template: ArrayList<Element>): Category {
            var category = Category()
            var listCategories = db!!.categoryDAO().getAll()


            for (i in listCategories) {

                if(i.isMain){
                    category.id = i.id
                    category.template = null
                }
            }

            category.list = getCategoryList(category.id!!, listCategories, template)


            return category
        }

        fun getToDoCategory(mainCategory: Category): Category {
            var todoList = Category()
            var listElement =  db!!.elementDAO().getToDoAll()
            var listFeature =  db!!.featureDAO().getAll()

            for (i in listElement) {

                var elem = Element()
                elem.id = i.id
                elem.title = i.title
                elem.list = getFeatureList(i.id!!, listFeature)
                elem.category = findCategory(i.cat_id!!, mainCategory)
                elem.image = i.image
                elem.indicator = i.indicator
                elem.todo = true

                todoList.list.add(elem)
            }

            return todoList
        }

        fun getTemplates(): ArrayList<Element> {
            var listTemplates =  db!!.elementDAO().getTemplateAll()
            var listFeature =  db!!.featureDAO().getAll()

            var list = ArrayList<Element>()

            for (i in listTemplates) {

                var elem = Element()
                elem.id = i.id
                elem.title = i.title
                elem.list = getFeatureList(i.id!!, listFeature)
                elem.category = null
                elem.image = i.image
                elem.indicator = i.indicator
                elem.todo = i.todo

                list.add(elem)
            }

            return list
        }

        fun getCategoryList(catId: Int, listCategories: List<CategoryEntity>, template: ArrayList<Element>) : ArrayList<ListItem> {

            var list = ArrayList<ListItem>()
            var listElement =  db!!.elementDAO().getAll()
            var category = Category()

            for(cat in listCategories){

                if(cat.cat_id!= null && cat.cat_id == catId){

                    category.title = cat.title
                    category.template = null

                    var listofCat = getCategoryList(cat.id!!, listCategories, template)
                    for(item in listofCat)
                        category.list.add(item)

                    list.add(category)
                }else if(cat.temp_id != null){
                    var templateCategory: Element? = null
                    for (temp in template) {
                        if (temp.id == cat.temp_id) {
                            templateCategory = temp
                            break
                        }
                    }

                    if (templateCategory != null) {
                        category.list = getElementsList(catId, listElement) as ArrayList<ListItem>
                    } else {

                    }
                }
            }
            return list
        }

        fun getElementsList(catId: Int, listElement: List<ElementEntity>) : ArrayList<Element>{

            var list = ArrayList<Element>()

            var listFeature =  db!!.featureDAO().getAll()

            for (el in listElement) {

                if(el.cat_id == catId) {

                    var elem = Element()
                    elem.id = el.id
                    elem.title = el.title
                    elem.list = getFeatureList(el.id!!, listFeature)
                    elem.category = null
                    elem.image = el.image
                    elem.indicator = el.indicator
                    elem.todo = el.todo

                    list.add(elem)

                }

            }
            return list
        }

        fun getFeatureList(elemId: Int, listFeature: List<FeatureEntity>) : ArrayList<Feature>{

            var list = ArrayList<Feature>()

            for (f in listFeature) {

                if(f.elem_id == elemId) {

                    var feature = Feature(f.title, f.detail)
                    feature.id = f.id
                    list.add(feature)

                }

            }
            return list
        }

        fun findCategory(index: Int, mainCategory: ListItem): Category? {

            if(mainCategory is Element)
                return null

            if((mainCategory as Category).id == index)
                return mainCategory

            var find :Category?= null
            for(i in (mainCategory).list){

                find = findCategory(index, i)
                if(find != null) break

            }
            return find
        }

        fun printDB(){

            var listCategories = db!!.categoryDAO().getAll()
            var listElement =  db!!.elementDAO().getAll()
            var listFeature =  db!!.featureDAO().getAll()

            println("---------------------------------------------- CATEGORY TABLE ----------------------------------------------")
            println("Elements: " + listCategories.size)
            println("id\t\t\t\tcat_id\t\t\t\ttemp_id\t\t\t\ttitle\t\t\t\tisMain")
            for(i in listCategories){
                println(i.id.toString()+"\t\t\t\t"+i.cat_id.toString()+"\t\t\t\t" + i.temp_id+"\t\t\t\t"+i.title+"\t\t\t\t"+i.isMain.toString())
            }
            println("------------------------------------------------------------------------------------------------------------")

            println("---------------------------------------------- ELEMENT TABLE -----------------------------------------------")
            println("Elements: " + listElement.size)
            println("id\t\t\t\tcat_id\t\t\t\ttitle\t\t\t\tindicator\t\t\t\ttodo")
            for(i in listElement){
                println(i.id.toString()+"\t\t\t\t"+i.cat_id.toString()+"\t\t\t\t" + i.title+"\t\t\t\t"+i.indicator+"\t\t\t\t"+i.todo.toString())
            }
            println("------------------------------------------------------------------------------------------------------------")

            println("---------------------------------------------- FEATURE TABLE -----------------------------------------------")
            println("Elements: " + listFeature.size)
            println("id\t\t\t\telem_id\t\t\t\ttitle\t\t\t\tdetail")
            for(i in listFeature){
                println(i.id.toString()+"\t\t\t\t"+i.elem_id.toString()+"\t\t\t\t" + i.title+"\t\t\t\t"+i.detail)
            }
            println("------------------------------------------------------------------------------------------------------------")

        }

        fun ToEntity(elemIn: Element): ElementEntity{
            var elem = ElementEntity()
            elem.id = elemIn.id
            elem.image = elemIn.image

            if(elemIn.category != null)
                elem.cat_id = elemIn.category!!.id
            else
                elem.cat_id = null

            elem.title= elemIn.title
            elem.indicator = elemIn.indicator
            elem.todo = elem.todo
            return elem
        }

        fun insertTemplates(listOfTemplate : ArrayList<Element>){
            for(temp in listOfTemplate){
                for(fe in temp.list)
                db!!.elementDAO().insertAll(ToEntity(temp))
            }
            printDB()
        }
    }
}
