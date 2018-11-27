package pl.polsl.project.catalogex.database

import android.app.Application
import android.arch.persistence.room.*
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.entity.CategoryEntity
import pl.polsl.project.catalogex.database.entity.ElementEntity
import pl.polsl.project.catalogex.database.entity.FeatureEntity
import java.io.File

class Utility : Application() {

    override fun onCreate() {
        super.onCreate()
        db =  Room.databaseBuilder(applicationContext, AppDatabase::class.java, databaseName).allowMainThreadQueries().build()
        dbPath = packageManager.getPackageInfo(packageName,0).applicationInfo.dataDir + "/databases"
        getAllLists()
    }

    companion object {

        private var databaseName: String = "database_catalogex_1.0"
        private var dbPath : String = ""
        private var db: AppDatabase? = null

        private var listTemplates : List<ElementEntity>? = null
        private var listCategories : List<CategoryEntity>? = null
        private var listTODO : List<ElementEntity>? = null
        private var listElement : List<ElementEntity>? = null
        private var listFeature : List<FeatureEntity>? = null

        fun getAllLists(){
            listTemplates =  db!!.elementDAO().getTemplateAll()
            listCategories = db!!.categoryDAO().getAll()
            listTODO = db!!.elementDAO().getToDoAll()
            listElement =  db!!.elementDAO().getElementsAll()
            listFeature =  db!!.featureDAO().getAll()
        }

        fun getTemplates(): ArrayList<Element> {

            var list = ArrayList<Element>()

            for (i in listTemplates!!) {

                var elem = i.toElement()
                elem.list = getFeatureList(i.id!!)
                list.add(elem)
            }
            return list
        }

        fun getMainCategory(template: ArrayList<Element>): Category {

            var category = Category()

            for (i in listCategories!!) {

                if(i.cat_id == null){
                    category.id = i.id
                    category.template = null
                    break
                }
            }

            category.list = getCategoryList(category, template)

            //If the database was cleared
            if(category.id == null){
                Utility.insertCategories(category,null,true)
            }

            return category
        }

        fun getToDoCategory(mainCategory: Category): Category {

            var todoList = Category()

            for (i in listTODO!!) {

                var elem = i.toElement()

                elem.list = getFeatureList(i.id!!)
                elem.category = findCategory(i.cat_id!!,mainCategory)

                todoList.list.add(elem)
            }

            return todoList
        }

        fun getCategoryList(parentCategory: Category, template: ArrayList<Element>) : ArrayList<ListItem> {

            var list = ArrayList<ListItem>()

            for(cat in listCategories!!){

                if(cat.cat_id!= null && cat.cat_id == parentCategory.id) {

                    var category = cat.toCategory(null,null)

                    if (cat.temp_id == null) {
                        category.list = getCategoryList(category,template)

                    } else if (cat.temp_id != null) {

                        var templateCategory: Element? = null

                        for (temp in template) {
                            if (temp.id == cat.temp_id) {
                                templateCategory = temp
                                break
                            }
                        }

                        category.template = templateCategory
                        category.list = getElementsList(category) as ArrayList<ListItem>

                    }

                    list.add(category)
                }
            }
            return list
        }

        fun getElementsList(parentCategory: Category) : ArrayList<Element>{

            var list = ArrayList<Element>()

            for (el in listElement!!) {

                if(el.cat_id == parentCategory.id) {

                    var elem = el.toElement(parentCategory,getFeatureList(el.id!!))
                    list.add(elem)

                }

            }
            return list
        }

        fun getFeatureList(elemId: Int) : ArrayList<Feature>{

            var list = ArrayList<Feature>()

            for (f in listFeature!!) {
                if(f.elem_id == elemId) {
                    var feature = f.toFeature()
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

        fun insertElement(element: Element){
            insertElementsList(arrayListOf(element))
        }

        fun insertElementsList(listOfElements : ArrayList<Element>){

            for(temp in listOfElements){

                var parentId: Int? = null
                if(temp.category != null)
                    parentId = temp.category!!.id

                temp.id = db!!.elementDAO().insert(temp.ToElementEntity(parentId)).toInt()

                for(fe in temp.list)
                    fe.id = db!!.featureDAO().insert(fe.ToFeatureEntity(temp.id)).toInt()
            }
        }

        fun insertCategories(category : Category, parentId: Int? = null, isMain: Boolean = false){

            if(category.template == null)
                category.id = db!!.categoryDAO().insert(category.ToCategoryEntity(parentId,null,isMain)).toInt()
            else{
                if(category.template!!.id == null)
                    insertElement(category.template!!)
                category.id = db!!.categoryDAO().insert(category.ToCategoryEntity(parentId,category.template!!.id,isMain)).toInt()
            }

            for(temp in category.list){
                if(temp is Element)
                    insertElement(temp)
                else if(temp is Category){
                    insertCategories(temp,category.id,false)
                }
            }
        }

        fun makeSomeSpace(str:String,long:Int = 25):String{
            var out = str
            for(i in str.length-1 until long){
                out+= ' '
            }
            return out
        }

        fun printDB(){

            var listCategories = db!!.categoryDAO().getAll()
            var listElement =  db!!.elementDAO().getAll()
            var listFeature =  db!!.featureDAO().getAll()

            println("---------------------------------------------- CATEGORY TABLE ----------------------------------------------")
            println("Elements: " + listCategories.size)
            println(makeSomeSpace("id") + makeSomeSpace("cat_id") + makeSomeSpace("temp_id") + makeSomeSpace("title"))
            for(i in listCategories){
                println(makeSomeSpace(i.id.toString())+makeSomeSpace(i.cat_id.toString())+makeSomeSpace(i.temp_id.toString())+makeSomeSpace(i.title))
            }
            println("------------------------------------------------------------------------------------------------------------")

            println("---------------------------------------------- ELEMENT TABLE -----------------------------------------------")
            println("Elements: " + listElement.size)
            println(makeSomeSpace("id") + makeSomeSpace("cat_id") + makeSomeSpace("title") + makeSomeSpace("indicator")+ makeSomeSpace("todo"))
            for(i in listElement){
                println(makeSomeSpace(i.id.toString())+makeSomeSpace(i.cat_id.toString())+makeSomeSpace(i.title)+makeSomeSpace(i.indicator.toString())+makeSomeSpace(i.todo.toString()))
            }
            println("------------------------------------------------------------------------------------------------------------")

            println("---------------------------------------------- FEATURE TABLE -----------------------------------------------")
            println("Elements: " + listFeature.size)
            println(makeSomeSpace("id") + makeSomeSpace("elem_id") + makeSomeSpace("detail") )
            for(i in listFeature){
                println(makeSomeSpace(i.id.toString())+makeSomeSpace(i.elem_id.toString())+makeSomeSpace(i.title)+makeSomeSpace(i.detail))
            }
            println("------------------------------------------------------------------------------------------------------------")

        }

        fun deleteDatabaseFile() {

            val databases = File(dbPath)
            val db = File(databases, databaseName)
            if (db.delete())
                println("Database deleted")
            else
                println("Failed to delete database")

        }
    }
}
