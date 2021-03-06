package pl.polsl.project.catalogex.database

import android.app.Application
import android.arch.persistence.room.*
import android.content.Context
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.data.ListItem
import pl.polsl.project.catalogex.database.entity.CategoryEntity
import pl.polsl.project.catalogex.database.entity.ElementEntity
import pl.polsl.project.catalogex.database.entity.FeatureEntity
import pl.polsl.project.catalogex.R
import pl.polsl.project.catalogex.display.ShowMainScreen
import java.io.File

//Klasa inicjalizująca połączenie z bazą danych oraz wykorzystywana do komunikacji z tabelami
@Suppress("unused")
class Utility : Application() {

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    //Metoda otwiera połączenie z bazą danych oraz pobiera dane
    private fun initialize(){
        appContext = this
        db =  Room.databaseBuilder(applicationContext, AppDatabase::class.java, databaseName).allowMainThreadQueries().build()
        dbPath = packageManager.getPackageInfo(packageName,0).applicationInfo.dataDir + "/databases"
        updateAllLists()
    }

    companion object {

        var databaseName: String = "database_cataloger_1.0"
        var dbPath : String = ""
        var appContext: Context? = null
        var db: AppDatabase? = null

        private var listTemplates : List<ElementEntity>? = null
        private var listCategories : List<CategoryEntity>? = null
        private var listTODO : List<ElementEntity>? = null
        private var listElement : List<ElementEntity>? = null
        private var listFeature : List<FeatureEntity>? = null

        //Metoda pobierająca wszystkie informacje z bazy danych
        fun updateAllLists(){
            listTemplates =  db!!.elementDAO().getTemplateAll()
            listCategories = db!!.categoryDAO().getAll()
            listTODO = db!!.elementDAO().getToDoAll()
            listElement =  db!!.elementDAO().getElementsAll()
            listFeature =  db!!.featureDAO().getAll()
        }

        //Metoda pobierająca wzory z bazy
        fun getTemplates(): ArrayList<Element> {

            val list = ArrayList<Element>()

            for (i in listTemplates!!) {

                val elem = i.toElement()
                elem.list = getFeatureList(i.id!!)
                list.add(elem)
            }
            return list
        }

        //Metoda pobierająca główną kategorię oraz rekonstruuje cała strukutrę danych
        fun getMainCategory(template: ArrayList<Element>): Category {

            val category = Category()

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

                //Initializing starting templates

                if(appContext != null) {
                    val templateBook = Element()
                    templateBook.title = appContext!!.getString(R.string.template_book)
                    templateBook.addFeature(Feature(appContext!!.getString(R.string.template_book_author), appContext!!.getString(R.string.example_Text)))
                    templateBook.addFeature(Feature(appContext!!.getString(R.string.template_book_publisher), appContext!!.getString(R.string.example_Text)))
                    templateBook.addFeature(Feature(appContext!!.getString(R.string.template_book_year), appContext!!.getString(R.string.example_Text)))
                    templateBook.addFeature(Feature(appContext!!.getString(R.string.template_book_binding), appContext!!.getString(R.string.example_Text)))
                    templateBook.addFeature(Feature(appContext!!.getString(R.string.template_book_pages), appContext!!.getString(R.string.example_Text)))
                    Utility.insertElement(templateBook)
                    ShowMainScreen.listOfTemplate.add(templateBook)

                    val templateMovie = Element()
                    templateMovie.title = appContext!!.getString(R.string.template_movie)
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_genre), appContext!!.getString(R.string.example_Text)))
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_time), appContext!!.getString(R.string.example_Text)))
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_director), appContext!!.getString(R.string.example_Text)))
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_scenario), appContext!!.getString(R.string.example_Text)))
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_country), appContext!!.getString(R.string.example_Text)))
                    templateMovie.addFeature(Feature(appContext!!.getString(R.string.template_movie_year), appContext!!.getString(R.string.example_Text)))
                    Utility.insertElement(templateMovie)
                    ShowMainScreen.listOfTemplate.add(templateMovie)

                    val templateMusic = Element()
                    templateMusic.title = appContext!!.getString(R.string.template_music)
                    templateMusic.addFeature(Feature(appContext!!.getString(R.string.template_music_artist), appContext!!.getString(R.string.example_Text)))
                    templateMusic.addFeature(Feature(appContext!!.getString(R.string.template_music_genre), appContext!!.getString(R.string.example_Text)))
                    templateMusic.addFeature(Feature(appContext!!.getString(R.string.template_music_time), appContext!!.getString(R.string.example_Text)))
                    templateMusic.addFeature(Feature(appContext!!.getString(R.string.template_music_year), appContext!!.getString(R.string.example_Text)))
                    templateMusic.addFeature(Feature(appContext!!.getString(R.string.template_music_carrier), appContext!!.getString(R.string.example_Text)))
                    Utility.insertElement(templateMusic)
                    ShowMainScreen.listOfTemplate.add(templateMusic)
                }

            }

            return category
        }

        //Metoda pobierająca listę TO DO
        fun getToDoCategory(mainCategory: Category): Category {

            val todoList = Category()

            for (i in listTODO!!) {

                val elem = i.toElement()

                elem.list = getFeatureList(i.id!!)
                elem.category = findCategory(i.cat_id!!,mainCategory)

                todoList.list.add(elem)
            }

            return todoList
        }

        //Metoda pobierająca listę kategorii
        private fun getCategoryList(parentCategory: Category, template: ArrayList<Element>) : ArrayList<ListItem> {

            val list = ArrayList<ListItem>()

            for(cat in listCategories!!){

                if(cat.cat_id!= null && cat.cat_id == parentCategory.id) {

                    val category = cat.toCategory(null,null)

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

                        @Suppress("UNCHECKED_CAST")
                        category.list = getElementsList(category) as ArrayList<ListItem>

                    }

                    list.add(category)
                }
            }
            return list
        }

        //Metoda pobierająca listę elementów dla kategorii
        private fun getElementsList(parentCategory: Category) : ArrayList<Element>{

            val list = ArrayList<Element>()

            for (el in listElement!!) {

                if(el.cat_id == parentCategory.id) {

                    val elem = el.toElement(parentCategory,getFeatureList(el.id!!))
                    list.add(elem)

                }

            }
            return list
        }

        //Metoda pobierająca atrybuty dla elementu
        private fun getFeatureList(elemId: Int) : ArrayList<Feature>{

            val list = ArrayList<Feature>()

            for (f in listFeature!!) {
                if(f.elem_id == elemId) {
                    val feature = f.toFeature()
                    list.add(feature)
                }
            }

            return list
        }

        //Metoda znajdująca kategorię
        private fun findCategory(index: Int, mainCategory: ListItem): Category? {

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

        //metoda dodająca przedmiot do bazy
        fun insertElement(element: Element){
            insertElementsList(arrayListOf(element))
        }

        //metoda dodająca listę przedmiotów do bazy
        private fun insertElementsList(listOfElements : ArrayList<Element>){

            for(temp in listOfElements){

                var parentId: Int? = null
                if(temp.category != null)
                    parentId = temp.category!!.id

                temp.id = db!!.elementDAO().insert(temp.toElementEntity(parentId)).toInt()

                for(fe in temp.list)
                    fe.id = db!!.featureDAO().insert(fe.toFeatureEntity(temp.id)).toInt()
            }
        }

        //metoda dodająca kategorię do bazy
        fun insertCategories(category : Category, parentId: Int? = null, isMain: Boolean = false){

            if(category.template == null)
                category.id = db!!.categoryDAO().insert(category.toCategoryEntity(parentId,null,isMain)).toInt()
            else{
                if(category.template!!.id == null)
                    insertElement(category.template!!)
                category.id = db!!.categoryDAO().insert(category.toCategoryEntity(parentId,category.template!!.id,isMain)).toInt()
            }

            for(temp in category.list){
                if(temp is Element)
                    insertElement(temp)
                else if(temp is Category){
                    insertCategories(temp,category.id,false)
                }
            }
        }

        //metoda usuwająca kategorię z bazy
        fun deleteCategories(category : Category){

            for(temp in category.list){
                if(temp is Element)
                    deleteElement(temp)
                else if(temp is Category){
                    deleteCategories(temp)
                }
            }

            db!!.categoryDAO().delete(category.toCategoryEntity())
            category.id = null
        }

        //metoda usuwająca element z bazy
        fun deleteElement(element: Element){
            deleteElementList(arrayListOf(element))
        }

        //metoda usuwająca listę elementów z bazy
        fun deleteElementList(listOfElements : ArrayList<Element>){

            for(temp in listOfElements){
                db!!.elementDAO().delete(temp.toElementEntity())
                temp.id = null

                for(feat in temp.list){
                    deleteFeature(feat)
                }
            }
        }

        //metoda usuwająca atrybut z bazy
        fun deleteFeature(feature: Feature){
            db!!.featureDAO().delete(feature.toFeatureEntity())
            feature.id = null
        }

        //metoda usuwająca wzór z bazy
        fun deleteTemplate(temp: Element){

            updateAllLists()

            val catIndexes = ArrayList<Int>()
            val elemIndexes = ArrayList<Int>()
            val featIndexes = ArrayList<Int>()

            for(cat in listCategories!!){
                if(cat.temp_id == temp.id){
                    catIndexes.add(cat.id!!)
                    db!!.categoryDAO().delete(cat)
                    cat.id = null
                }
            }

            for(elem in listElement!!){
                if(catIndexes.contains(elem.cat_id)){
                    elemIndexes.add(elem.id!!)
                    db!!.elementDAO().delete(elem)
                    elem.id = null
                }
            }

            for(elem in listTODO!!){
                if(catIndexes.contains(elem.cat_id)){
                    elemIndexes.add(elem.id!!)
                    db!!.elementDAO().delete(elem)
                    elem.id = null
                }
            }

            for(feat in listFeature!!){
                if(elemIndexes.contains(feat.elem_id) || temp.id == feat.elem_id){
                    featIndexes.add(feat.id!!)
                    db!!.featureDAO().delete(feat)
                    feat.id = null
                }
            }

            db!!.elementDAO().delete(temp.toElementEntity())
            temp.id = null
        }

        //metoda formatująca tekst do odpowiedniej długości
        private fun formatString(str:String, long:Int = 25):String{
            var out = str
            for(i in str.length-1 until long){
                out+= ' '
            }
            return out
        }

        //metoda wykorzystywana do debbugowania bazy danych, służy do wyświetlania jej zawartości
        fun printDB() :String{

            var str = ""

            val listCategories = db!!.categoryDAO().getAll()
            val listElement =  db!!.elementDAO().getAll()
            val listFeature =  db!!.featureDAO().getAll()

            str+= ("---------------------------------------------- CATEGORY TABLE ----------------------------------------------\n")
            str+= ("Elements: " + listCategories.size+"\n")
            str+= (formatString("id") + formatString("cat_id") + formatString("temp_id") + formatString("title")+"\n")
            for(i in listCategories){
                str+= (formatString(i.id.toString())+formatString(i.cat_id.toString())+formatString(i.temp_id.toString())+formatString(i.title)+"\n")
            }
            str+= ("------------------------------------------------------------------------------------------------------------\n")

            str+= ("---------------------------------------------- ELEMENT TABLE -----------------------------------------------\n")
            str+= ("Elements: " + listElement.size+"\n")
            str+= (formatString("id") + formatString("cat_id") + formatString("title") + formatString("indicator")+ formatString("todo")+"\n")
            for(i in listElement){
                str+= (formatString(i.id.toString())+formatString(i.cat_id.toString())+formatString(i.title)+formatString(i.indicator.toString())+formatString(i.todo.toString())+"\n")
            }
            str+= ("------------------------------------------------------------------------------------------------------------\n")

            str+= ("---------------------------------------------- FEATURE TABLE -----------------------------------------------\n")
            str+= ("Elements: " + listFeature.size+"\n")
            str+= (formatString("id") + formatString("elem_id") + formatString("detail")+"\n")
            for(i in listFeature){
                str+= (formatString(i.id.toString())+formatString(i.elem_id.toString())+formatString(i.title)+formatString(i.detail)+"\n")
            }
            str+= ("------------------------------------------------------------------------------------------------------------\n")
            return str
        }

        //metoda wykorzystywana do debbugowania bazy danych, usuwa wszystkie jej pliki.
        fun deleteDatabaseFile() {

            val databases = File(dbPath)
            val db = File(databases, databaseName)
            if (db.delete()) {
                println("DB deleted")
            }

        }
    }
}
