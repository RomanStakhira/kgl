import kgl.*
import kotlin.test.*


class AlgorithmsTest {
    val puc811 = MutableGraph("puc811",false)
    val puc812 = MutableGraph("puc812",true)
    val puc81 by lazy { puc811 + puc812 }

    val w = MutableGraph("wiki",false)
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
        w.apply {
            connect(1,2,Eint(7,"1st"))
            connect(2,4,Eint(15))
            connect(4,5,Eint(6))
            connect(5,6,Eint(9))
            connect(6,1,Eint(14))
            connect(1,3,Eint(9))
            connect(2,3,Eint(10))
            connect(4,3,Eint(11))
            connect(6,3,Eint(2))
            //toGraphviz().show()
        }
    }
    @Test
    fun dfsTest(){
        puc811.dfs("x1","x3")
        //println("${g.neighbors(2)}")

    }
    @Test
    fun testDijkstra(){
        //println("$w")
        w.dijkstra(1,5)

    }
}