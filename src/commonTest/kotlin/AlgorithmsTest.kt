import kgl.MutableGraph
import kgl.dfs
import kotlin.test.*


class AlgorithmsTest {
    val g = MutableGraph("G1",directed = false)

    @BeforeTest
    fun Init(){
        g.addPath(1,2,3)
        g.addPath(2,4,5,6,1)
    }
    @Test
    fun dfsTest(){
        g.dfs(2,6)
        //println("${g.neighbors(2)}")

    }
}