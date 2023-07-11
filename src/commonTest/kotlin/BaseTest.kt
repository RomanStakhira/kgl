import utils.show
import utils.toGraphViz
import kotlin.test.*
val directionList = listOf(true,false,null)
class BaseTest {
    @Test
    fun testClone(){
       directionList.forEach{
            val g = MutableGraph("src",false)
            g.apply {
                g.connect(1,2)
                g.connect(2,3)
                g.connect(2,3,EDbl(9.0,"239"))
                g.connect(3,1)
                g.addVertices("S")
           }
        val gc = g.clone("copy")
        assertNotSame(g.vertices, gc.vertices, "Same vertices !")
        assertEquals(g,gc)
       }
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

            g2.connect(1, 4, EInt(5))
            // some edges are null
            assertFalse(g2.weightsPositive, "Weights Positive !!!")
            g2.setEdges(EInt(8, "blah"))
            g2.connect(4, 2, edgeDefaultInt)
            g2.connect(4, 4, EDbl(100.0))
            assertTrue(g2.weightsPositive, "Weights Positive !!!")
            assertTrue(g2.hasNoose)
            g2.rmVertices(4)
            assertFalse(g2.hasNoose)
        }
    }
    @Test
    fun testDisconnect(){
        directionList.forEach { d ->
            val g = MutableGraph("testDisconnect", d).also{
            it.apply {
                connect(1,2)
                connect(1,2,EInt(1,"int"))
                connect(1,2,EDbl(10.0,"double"))
                connect(1,2,Edge<Short>(8))
                //clone("$d").toGraphViz().show()
            }
        }
        g.disconnect(1,2,EDbl(10.0,"double"))
        assertEquals(3,g.getEdges(1,2).size,"Bad disconnect")
        g.disconnectAll(1,2)
        assertTrue(g.getEdges(1,2).isEmpty(),"Bad disconnectAll")
        }
    }
    @Test
    fun testOperators() {
        directionList.forEach { d1 ->
            directionList.forEach { d2 ->
                val g1 = MutableGraph("first", d1)
                g1.connect(1, 2)
                val c = 'a'
                g1 += c
                assertTrue(c in g1, "")

                val g2 = MutableGraph("second", d2)
                g2.apply {
                    connect(1, 2)
                    connect(1, 2, EDbl(2.0, "g"))
                }

                val g3 = g1 + g2 -c
                if ( d1 == d2){
                    g1+=g2
                    g1-=c
                    assertEquals(g3, g1,"g1\t$d1 $d2")
                }else{
                    val g4 = g2 + g1
                    val g5 = g4 - c
                    assertEquals(g3, g5, "g4\t$d1 $d2")
                    assertFails {
                        g2-=g1
                        g2+=g1
                    }
                }
                g3-=2
                assertTrue(2 !in g3)

                g1.apply {
                    connect(4,1,EInt(10,"Int"))
                    connect(1,2,EDbl(99.0))
                    connect(1, c, edgeDefaultInt)
                    connect("Hello", "Hello")
                    connect(2,3)
                    connect(1,4,EInt(10,"Int"))
                }
                g1 += 0.9
                assertContains(g1.getEdges(1, c), edgeDefaultInt)

                g2.apply {
                    clear()
                    connect(1,4,EInt(10,"Int"))
                    connect(1,2,EDbl(99.0))
                    connect(1,c,edgeDefaultInt)
                    addVertices(0.9,"Hello")
                    connect(4,1,EInt(10,"Int"))
                    connect(1,10000)
                }

                val g11 = g1 - g2
                println("$g11")
                if (d1 == d2) {
                    g1-=g2
                    assertTrue("Hello" in g1)
                    g1.apply {
                        name = "1$d1$d2"
                        //toGraphViz().show()
                    }
                } else{


                }

            }
        }
   }
}
