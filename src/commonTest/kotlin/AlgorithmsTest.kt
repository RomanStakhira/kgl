import kgl.*
import kgl.utils.*
import kotlin.test.*


class AlgorithmsTest {
    val puc811 = MutableGraph("puc811",false)
    val puc812 = MutableGraph("puc812",true)
    val puc81 by lazy { puc811 + puc812 }

    val w = MutableGraph("wiki",false)

    val crown = MutableGraph("crown",false)
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
        
        crown.apply {
            addNeighbors(1,6,7,8)
            addNeighbors(2,5,7,8)
            addNeighbors(3,5,6,8)
            addNeighbors(4,5,6,7)
        }
    }
    @Test
    fun dfsTest(){
        puc811.dfs("x1","x3")
        //println("${g.neighbors(2)}")

    }
    @Test
    fun dijkstra(){
        w.apply {
            connect(1,2,Edge(7,))
            connect(2,4,Edge(15))
            connect(4,5,Edge(6))
            connect(5,6,Edge(9))
            connect(6,1,Edge(14))
            connect(1,3,Edge(9))
            connect(2,3,Edge(10))
            connect(4,3,Edge(11))
            connect(6,3,Edge(2))
        }
        /*
        val cm = mapOf<Any,Int>(1 to 1, 2 to 2, 3 to 3)
        w.toGraphviz(colorMap = cm).show("C:\\tmp", LayoutEngines.sfdp)
        println("$w  ${w.weightsPositive}")
         */
        w.dijkstra(5)



    }

    @Test
    fun greedyColoring(){
        val puc43 = MutableGraph("puc43_Gamma_3",false)
        puc43.apply {
            addPath(1,2,3,4,5,6,7,1)
            addPath(1,3,6)
            addPath(4,3,7)
            connect(1,5)
            connect(4,7)
            addVertices('A')
            //connect('A','A')
            connect('a','b')
            connect('a',1)
            connect('a',2)
            connect('b',6)
            addNeighbors('X',1,2,3,7)
        }
        val cm = puc43.greedyColoring(8,'1')
        println("$cm  \n chromaticNumber=${puc43.chromaticNumber} \n Noose =  ${puc43.hasNoose}")
        puc43.toGraphviz(colorMap = cm).show("C:\\tmp", LayoutEngines.fdp)
        //assertEquals(4, puc43.chromaticNumber,"Wrongch Comatic Number!")


    }

}

