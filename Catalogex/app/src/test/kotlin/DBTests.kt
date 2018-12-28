import android.arch.persistence.room.Room
import junit.framework.Assert.assertEquals
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import pl.polsl.project.catalogex.data.Category
import pl.polsl.project.catalogex.data.Element
import pl.polsl.project.catalogex.data.Feature
import pl.polsl.project.catalogex.database.AppDatabase
import pl.polsl.project.catalogex.database.Utility
import pl.polsl.project.catalogex.display.ShowMainScreen
import java.io.IOException
import kotlin.system.measureTimeMillis

@Suppress("DEPRECATION", "ConvertToStringTemplate", "unused")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner::class)
class DBTests{

    companion object {

        private lateinit var db: AppDatabase
        private val timeInsertList :ArrayList<Long> = arrayListOf()
        private val timeReadList :ArrayList<Long> = arrayListOf()
        private val timeDeleteList :ArrayList<Long> = arrayListOf()
        private val numberOfElement: ArrayList<Int> = arrayListOf()
        private val template = Element()
        private val category1 = Category()
        private val category2 = Category()
        private val listOfElements : ArrayList<Element> = arrayListOf()
        private const val howMany = 1000

        @BeforeClass
        fun createDbConnectioon() {
            println("TESTING CONNECTIONS BEETWEEN OBJECT")
            Utility.db = Room.inMemoryDatabaseBuilder(
                    RuntimeEnvironment.application, AppDatabase::class.java).allowMainThreadQueries().build()
        }

        @AfterClass
        @Throws(IOException::class)
        fun closeDbConnectioon() {
            db.close()

            println("Number of elements: " + numberOfElement)
            println("Insertion time in [ms]: " + timeInsertList)
            println("Read time in [ms]: " + timeReadList)
            println("Delete time in [ms]: " + timeDeleteList)

            println("TESTING SUCCESFULLY")
        }
    }

    @Test
    fun dbTest1InsertTemplate() {

        println("Insert Template Test")

        Utility.updateAllLists()
        ShowMainScreen.listOfTemplate = Utility.getTemplates()
        ShowMainScreen.mainCategory = Utility.getMainCategory(ShowMainScreen.listOfTemplate)
        ShowMainScreen.todoList = Utility.getToDoCategory(ShowMainScreen.mainCategory)

        template.title = "TEST"
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        Utility.insertElement(template)

        //4 because 3 is predefined
        assertEquals("Assert template: " + Utility.db!!.elementDAO().getTemplateAll().size,4 == Utility.db!!.elementDAO().getTemplateAll().size && 22 == Utility.db!!.featureDAO().getAll().size, true)

        println("Template test DONE")
    }

    @Test
    fun dbTest2InsertCategory() {
        println("Insert Template Test")

        ShowMainScreen.mainCategory.list.add(category1)
        category1.list.add(category2)
        category2.template = template

        Utility.insertCategories(category1,ShowMainScreen.mainCategory.id)
        Utility.insertCategories(category2, category1.id)

        //4 because 3 is predefined
        assertEquals("Assert category: " + Utility.db!!.categoryDAO().getAll().size,2 == Utility.db!!.categoryDAO().getAll().size, true)

        println("Template test DONE")
    }

    @Test
    fun dbTest3InsertElements() {

        println("Insert Element Test")

        val elementSize = Utility.db!!.elementDAO().getElementsAll().size
        val featureSize = Utility.db!!.featureDAO().getAll().size

        for (index in 0 until howMany) {
            numberOfElement.add(index+1)
            val timeInsert = measureTimeMillis {

                val element = template.copy() as Element
                element.title = index.toString()
                element.category = category2
                category2.list.add(element)
                listOfElements.add(element)

                Utility.insertElement(element)
            }
            timeInsertList.add(timeInsert)

            val readTime = measureTimeMillis {
                Utility.updateAllLists()
                ShowMainScreen.listOfTemplate = Utility.getTemplates()
                ShowMainScreen.mainCategory = Utility.getMainCategory(ShowMainScreen.listOfTemplate)
                ShowMainScreen.todoList = Utility.getToDoCategory(ShowMainScreen.mainCategory)

            }

            assertEquals("Assert element: " + index + " size: " + Utility.db!!.elementDAO().getElementsAll().size + " should be: " + (elementSize + index+1)
                    + " feature: " + Utility.db!!.featureDAO().getAll().size + " should be: " + (featureSize + (index+1) * 6 + 16),
                    (elementSize + index+1 == Utility.db!!.elementDAO().getElementsAll().size
                    && featureSize + (index+1) * 6 + 16 == Utility.db!!.featureDAO().getAll().size), true)

            timeReadList.add(readTime)
        }
        println(howMany.toString() + " elements inserted")
    }

    @Test
    fun dbTest4DeleteElements() {

        println("Delete Category Test")
        listOfElements.clear()

        for (index in 0 until howMany) {

            val element = template.copy() as Element
            element.title = index.toString()
            element.category = category2
            category2.list.add(element)
            listOfElements.add(element)

            Utility.insertElement(element)

        }

        val elementSize = Utility.db!!.elementDAO().getElementsAll().size
        val featureSize = Utility.db!!.featureDAO().getAll().size

        for(elem in 0 until howMany) {
            val delTime = measureTimeMillis {
                Utility.deleteElement(listOfElements[elem])
            }
            timeDeleteList.add(delTime)


            assertEquals("Delete element: " + elem + " size: " + Utility.db!!.elementDAO().getElementsAll().size
                    + " should be: " + (elementSize-(elem+1)).toString()
                    + " feature: " + Utility.db!!.featureDAO().getAll().size
                    + " should be: " + (featureSize - (elem+1)*6).toString(),
                    (elementSize-(elem+1) == Utility.db!!.elementDAO().getElementsAll().size
                            && featureSize - (elem+1)*6 == Utility.db!!.featureDAO().getAll().size) ,true)

        }
        println(howMany.toString() + " elements deleted")

    }

    @Test
    fun dbTest5DeleteCategory() {

        println("Delete Element Test")
        listOfElements.clear()

        val categorySize = Utility.db!!.categoryDAO().getAll().size

        Utility.updateAllLists()
        ShowMainScreen.listOfTemplate = Utility.getTemplates()
        ShowMainScreen.mainCategory = Utility.getMainCategory(ShowMainScreen.listOfTemplate)
        ShowMainScreen.todoList = Utility.getToDoCategory(ShowMainScreen.mainCategory)

        val featureSize = Utility.db!!.featureDAO().getAll().size
        val elementSize = Utility.db!!.elementDAO().getElementsAll().size

        Utility.insertElement(template)

        ShowMainScreen.mainCategory.list.add(category1)
        category1.list.add(category2)
        category2.template = template

        Utility.insertCategories(category1,ShowMainScreen.mainCategory.id)
        Utility.insertCategories(category2, category1.id)

        for (index in 0 until howMany) {

            val element = template.copy() as Element
            element.id = null
            element.title = index.toString()
            element.category = category2
            category2.list.add(element)
            listOfElements.add(element)

            Utility.insertElement(element)

        }

        Utility.deleteCategories(category1)

        assertEquals("Delete category: " ,
                categorySize == Utility.db!!.categoryDAO().getAll().size
                        && featureSize == Utility.db!!.featureDAO().getAll().size
                        && elementSize == Utility.db!!.elementDAO().getElementsAll().size, true)


        println("Category Delete DONE")

    }

    @Test
    fun dbTest6DeleteTemplate() {

        println("Delete Template Test")

        Utility.updateAllLists()
        ShowMainScreen.listOfTemplate = Utility.getTemplates()
        ShowMainScreen.mainCategory = Utility.getMainCategory(ShowMainScreen.listOfTemplate)
        ShowMainScreen.todoList = Utility.getToDoCategory(ShowMainScreen.mainCategory)

        template.title = "TEST"
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        template.addFeature(Feature("TEST", "TEST"))
        Utility.insertElement(template)

        Utility.insertCategories(category2,ShowMainScreen.mainCategory.id)

        Utility.deleteTemplate(template)
        assertEquals("Delete template: " + Utility.db!!.elementDAO().getTemplateAll().size,3 == Utility.db!!.elementDAO().getTemplateAll().size && 16 == Utility.db!!.featureDAO().getAll().size
                && 1 == Utility.db!!.categoryDAO().getAll().size, true)

        println("Template test DONE")
    }
}
