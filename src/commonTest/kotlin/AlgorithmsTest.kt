import kgl.Graph
import kgl.MutableGraph
import kgl.dfs
import kotlin.test.*


class AlgorithmsTest {
    val puc811 = MutableGraph("puc811",false)
    val puc812 = MutableGraph("puc812",true)
    val puc81 by lazy { puc811 + puc812 }
    @BeforeTest
    fun Init(){
        puc811.apply {
            addPath("x1","x2","x3","x4","x5")
            addPath("x2","x9","x8","x1")
            addPath("x2","x7","x4")
            addPath("x3","x6","x5")
            addPath("x6","x7","x9")
            addPath("x6","x9","x1")
            addPath("x6","x8","x1")
        }

        puc812.apply {
            connect("x1","x7")
            connect("x3","x9")
            connect("x4","x6")
            connect("x8","x5")
        }
        puc81.name="puc81"
        //println("$puc81")

    }
    @Test
    fun dfsTest(){
        puc811.dfs("x1","x3")
        //println("${g.neighbors(2)}")

    }
}