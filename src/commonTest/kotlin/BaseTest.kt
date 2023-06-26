import utils.show
import utils.toGraphViz
import kotlin.test.*
val directionList = listOf(true,false,null)
class BaseTest {
    @BeforeTest
    fun init(){
    }

    @Test
    fun testCopy(){
       // directionList.forEach{
            val g = MutableGraph("src",false)
            g.apply {
                g.connect(1,2)
                g.connect(2,3)
                g.connect(2,3,EDbl(9.0,"239"))
                g.connect(3,1)
                g.addVertices("S")
                toGraphViz().show("C:\\tmp")
           }
        val gc = g.clone("copy")
       // assertSame(g.vertices, gc.vertices, "Same vertices !")
        g.disconnect(2,3)
        g.rmVertices("S")
        gc.toGraphViz().show("C:\\tmp")
        assertNotEquals(g,gc)
       // }

    }
    @Test
    fun testEquals(){
        directionList.forEach {
            val g1 = MutableGraph("g1",it)
            val g2 = MutableGraph("g2", false)
            g1.addVertices(3, 2, 1)
            g2.addNeighbors(1, 2, 3)
            assertNotEquals(g1, g2, "Equals !\n")
            g1.connect(1, 3)
            g1.connect(1, 2)
            g1.connect(3, 1)
            g1.connect(2, 1)
            g2.connect(3, 9, EInt(1))
            g1.connect(3, 9, EInt(1))
            g1.connect(9, 3, EInt(1))
            assertEquals(g1, g2, "Equals !\n")

            //println("$ANSI_BLUE_BACKGROUND$ANSI_BLACK${g1 == g2}")

            g2.connect(1, 4, EInt(5))
            // some edges are null
            assertFalse(g2.weightsPositive, "Weights Positive !!!")
            g2.setEdges(EInt(8, "blah"))
            g2.connect(4, 2, edgeDefaultInt)
            g2.connect(4, 4, EDbl(100.0))
            assertTrue(g2.weightsPositive, "Weights Positive !!!")
            assertTrue(g2.hasNoose)
            g2 -= 4
            assertFalse(g2.hasNoose)
        }
    }
    @Test
    fun testDisconnect(){
        listOf(true,false,null).forEach { d ->
            val g = MutableGraph("testDisconnect", d).also{
            it.apply {
                connect(1,2)
                connect(1,2,EInt(1,"int"))
                connect(1,2,EDbl(10.0,"double"))
                connect(1,2,Edge<Short>(8))
                clone("$d").toGraphViz().show("C:\\tmp")
            }
        }
        g.disconnect(1,2,EDbl(10.0,"double"))
        assertEquals(3,g.getEdges(1,2).size,"Bad disconnect")
        g.disconnectAll(1,2)
        assertTrue(g.getEdges(1,2).isEmpty(),"Bad disconnectAll")
        }
    }
    @Test
    fun testOperators(){
        listOf(true,/*false,null*/).forEach { d ->
            val g = MutableGraph("testOperators", d).also {
                it.apply {
                    addNeighbors(1,2,3)
                    addPath(2,4,5)
                    connect(3,5, edgeDefaultInt)
                    connect(3,5, EDbl(35.0, "test"))
                    connect(1,4)
                    connect(4,2, EInt(66, "label64"))
                    connect(5,"Hello 5", edgeDefaultInt)
                    //copy("g_$d").toGraphViz().show("C:\\tmp")
                }
            }
            g+='C'
            assertTrue('C' in g)
            g-=1
            g-=77
            assertTrue(1 !in g)
            assertTrue(77 !in g)
            val gg = g.clone()
            g.disconnect(3,5, edgeDefaultInt)
            gg-='C'
            g.toGraphViz().show("C:\\tmp")
            gg.toGraphViz().show("C:\\tmp")

        }
       /*

        val gResult1 : MutableGraph = g  - 6 + 'A' - 2
        assertFalse(2 in gResult1)
        assertTrue('A' in gResult1)
        //gResult1.toGraphViz().show("C:\\tmp")

        init()

        val gAdd1 = MutableGraph("Add1",false).also {
            it.apply {
                connect(5, 'a', edgeDefaultInt)
                connect('b', 'a', EDbl(2.1, "ba"))
                connect(2, 9.9, EDbl(9.9, "Double"))
            }
        }
        val gAdd2 = MutableGraph("Add2",true).also {
            it.apply {
                connect(5, 'a', edgeDefaultInt)
                connect('b', 'a', EDbl(2.1, "ba"))
                connect(2, 9.9, EDbl(9.9, "Double"))
            }
        }

        val gResult2 = g + gAdd2
        assertTrue(9.9 in gResult2)

        g+=gAdd2
        assertEquals(gResult2, g)

        assertFails{
            g+=gAdd1 // the directed properties do not match
        }

        init()
        // mixed Graph
        val gResult3 = g + gAdd1
        assertNull(gResult3.directed)
        val gResult4 = gResult3 + gAdd1
        assertNull(gResult4.directed)
        val gResult5 = gResult3 + gAdd2
        assertNull(gResult5.directed)

        //gResult5.toGraphViz().show("C:\\tmp")
        //g.toGraphViz().show("C:\\tmp")

        init()
        val gg = g.copy()
       // g -="99"
        gg -= 9.9
        //gg.disconnectAll(3,5)
        gg.toGraphViz().show("C:\\tmp")
        g.toGraphViz().show("C:\\tmp")

        //println("$g  \n weightsPositive = ${g.weightsPositive}")
        println("${false}")
*/    }


}