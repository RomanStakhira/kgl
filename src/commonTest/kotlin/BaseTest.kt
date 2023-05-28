import kgl.utils.*
import kotlin.test.*

class BaseTest {
    val g = MutableGraph("g", directed = true)

    @BeforeTest
    fun init(){
        g.apply {
            addNeighbors(1,2,3,4)
            addPath(4,5,6)
            connect(3,5, edgeDefaultInt)
            connect(2,4)
            connect(3,5, EDbl(9.0, "test"))
            connect(6,4, EInt(66, "label64"))
            connect(5,"Hello 5", edgeDefaultInt)
        }
    }
    @Test
    fun testEquals(){
        val g1 = MutableGraph("g1")
        val g2 = MutableGraph("g2",false)
        g1.addVertices(3,2,1)
        g2.addNeighbors(1,2,3)
        assertNotEquals(g1, g2,"Equals !\n")
        g1.connect(1,3)
        g1.connect(1,2)
        g1.connect(3,1)
        g1.connect(2,1)
        g2.connect(3,9, EInt(1))
        g1.connect(3,9, EInt(1))
        g1.connect(9,3, EInt(1))
        assertEquals(g1, g2,"Equals !\n")

        println("$ANSI_BLUE_BACKGROUND$ANSI_BLACK${g1.equals(g2)}")

        g2.connect(1,4, EInt(5))
        // some edges are null
        assertFalse(g2.weightsPositive,"Weights Positive !!!")
        g2.setEdges(EInt(8, "blah"))
        g2.connect(4,2, edgeDefaultInt)
        g2.connect(4,4, EDbl(100.0))
        assertTrue(g2.weightsPositive,"Weights Positive !!!")
        assertTrue(g2.hasNoose)
        g2-=4
        assertFalse(g2.hasNoose)
    }
    @Test
    fun testOperators(){
        g += "99"
        g -= 1
        g -= 77
        assertTrue("99" in g)
        assertTrue(77 !in g)
        assertFalse(1 in g)
        assertFalse(2 !in g)
        val gResult1 : MutableGraph = g  - 6 + 'A' - 2
        assertFalse(2 in gResult1)
        assertTrue('A' in gResult1)
        //gResult1.toGraphviz().show("C:\\tmp")

        g.clear()
        init()
        g-=1

        val gAdd = MutableGraph("Add",true)
        gAdd.apply {
            connect(5,'a', edgeDefaultInt)
            connect('b','a', EDbl(2.1, "ba"))
            connect(2,9.9, EDbl(9.9, "Double"))
        }
        val gResult2 = g + gAdd
        assertTrue(9.9 in gResult2)

        g+=gAdd
//
//        gResult2.toGraphviz().show("C:\\tmp")
//        g.toGraphviz().show("C:\\tmp")

assertEquals(gResult2, g)

        //println("$g  \n weightsPositive = ${g.weightsPositive}")
        println(g == gAdd)
    }


}