import kgl.MutableGraph
import kgl.edgeDefaultInt
import kotlin.test.*

class BaseTest {
    val gO = MutableGraph("Oriented")
    val g = MutableGraph("Graph", oriented = false)
    @Test
    fun test1(){
        gO.addVertices(1,'a',"B")
        gO.connect('A',1,edgeDefaultInt)
        println("$gO")
    }
}