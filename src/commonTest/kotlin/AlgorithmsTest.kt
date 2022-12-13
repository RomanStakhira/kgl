import kgl.MutableGraph
import kotlin.test.*

class AlgorithmsTest {
    val g = MutableGraph("G1")

    @BeforeTest
    fun Init(){
        g.addVertices(1,2,3)
    }
    @Test
    fun dfs(){
        println("$g")
    }
}