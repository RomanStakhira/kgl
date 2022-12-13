import kgl.MutableGraph
import kgl.edgeDefaultInt
import kotlin.test.*

class BaseTest {
    val gO = MutableGraph("Oriented")
    val g = MutableGraph(oriented = false)
    @Test
    fun test1(){
        gO.addVertices(1,'a',"B")
        gO.connect('A',1,edgeDefaultInt)
        gO.addNeighbors(1,2,3,"B")
        println("$gO  \n weightsPositive = ${gO.weightsPositive}")
    }

    @Test
    fun test2(){
        val g1 = MutableGraph()
        val g2 = MutableGraph()
        g1.addVertices(3,2,1)
        g2.addNeighbors(1,2,3)
        assertFalse(g1.equals(g2))
        g1.connect(1,2)
        g1.connect(1,3)
        assertTrue(g1.equals(g2))
    }
}