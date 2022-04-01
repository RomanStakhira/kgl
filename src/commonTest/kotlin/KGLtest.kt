package kgl

import kotlin.test.*

class KGLtest {
    val go = MutableGraph("testOriented")
    val gIn = MutableGraph("Inner Graph", oriented = false)

    data class test1(val x: Int, val y: Int) {
        val z = x + y
    }
    val O1 = object {
        val x = 1
        override fun toString(): String {
            return "Object $x"
        }
    }
    val c1 = test1(1, 2)

    @BeforeTest
    fun Init() {
        gIn.addNeighbors("A", "a")
        gIn.connect("B", "a")

        go.apply {
            addVertices("A", "B", "C")
            addVertices("a", "B", 1, 2, 3.14, 9L)
            addNeighbors("B", c1, 7, 8L, 8, "B", "B")
            connect("C", "a", Edbl(1.0, "Ca"))
            addNeighbors(c1, 2, 8L, "A")
            addNeighbors("a", 2, O1, gIn)
            addNeighbors("A", "a", 9L)
            addNeighbors(gIn, gIn, "a", 1, 2, "B", 1)
            connect("B", 8, Eint(8, "B8"))
        }
    }

    @Test
    fun testGraph() {
        val classUnderTest = MutableGraph()
        assertNotNull(classUnderTest)
        go.connect('z', "B", Eint(22, "char"))
        val goc = go.copy()
        /*
        goc.setEdges(edgeDefaultInt)
        go.toGraphviz().show()
        goc.toGraphviz().show(engine = LayoutEngines.twopi)
         */
    }

    @Test
    fun testBasic() {
        assertEquals(go.size, 14, "Incorrect Vertex number !!!")
        go.rmVertices("a", 1, 2, 8L, 9L, " NNN ")
        assertEquals(go.size, 9, "Incorrect Vertex number !!!")
    }

    @Test
    fun testsNeighbor() {
        go.rmVertices("a", 1, 2, 8L, 9L, " NNN ")
        val neighborTest = setOf<Any>(c1, 8, 7, "B")
        assertEquals(neighborTest, go.neighbors("B"), "Neighbor Error 1")

        go.rmVertices(8)
        go.connect(8, "B", Eint(2, "8"))
        assertEquals(neighborTest.minus(8), go.neighbors("B"), "Neighbor Error 2")
    }

    @Test
    fun testGetEdge() {
        val ed = Edbl(1.0, "Double1")
        val ei1 = Eint(1, "Integer1")
        val ei2 = Eint(2, "Integer2")
        go.connect("A", "B")
        go.connect("A", "B", ed)
        go.connect("A", "B", ei1)
        go.connect("A", "B", ei2)
        go.connect("B", "A", Eint(5))
        go.connect("A", "3", Eint(5))
        var edgeTest = setOf(ed, ei1, ei2, null)
        val edgeRes = go.getEdge("A", "B")
        assertEquals(edgeTest, edgeRes, "Incorrect Edge test 1 !")

        // Disconnect
        go.disconnect("A", "B", ed)
        go.disconnect("A", "B", ei1)
        assertEquals(edgeTest.minus(ed).minus(ei1), go.getEdge("A", "B"), "Incorrect Edge test 2 !")
    }

    @Test
    fun testAnalise() {
        val gA = MutableGraph("", null)
        gA.connect(2, 3, Eint(2))
        gA.connect(3, 5, Edbl(2.0))
        assertTrue(gA.weightsPositive, "Weights Positive")
        gA.connect(3, 4)
        gA.connect(1, 2, Eint(-11))
        assertFalse(gA.hasNoose, "Has no cycle")
        gA.connect(3, 3, Eint(77, "cycle 33"))
        gA.connect(5, 5)
        assertFalse(gA.weightsPositive, "Weights Negative")
        assertTrue(gA.hasNoose, "Has cycle")
    }

    @Test
    fun testArithmetic() {
        println("\n\t-----------------------------------------------------------------")
        val go1 = MutableGraph("go1", true)
        go1.apply {
            connect(1, 2, Eint(1, "go1"))
            addNeighbors(2,2,'z', 3, 1)
            connect(3, 1.1, edgeDefaultInt)
            connect('A','A')
            addVertices(9f)
            setEdge(1,2,Eint(1, "go1"),Edbl(100.0,"setEdge"))
            //toGraphviz().show()
        }
        val go2 = MutableGraph("go2", true)
        go2.apply {
            connect(1, 'A', Edbl(4.0))
            addNeighbors(2,3,'x')
            connect(3, 'B', Edbl(label = "label(3B)"))
            setEdge(3,'B',Edbl(7.0, "3-B"))
            //toGraphviz().show()
        }
        val gSum = go1 + go2
        gSum.name = "SumGo"
        //gSum.toGraphviz(concentrate = false).show()
        println("${gSum.neighbors(2)}")
//----------------------------------------------------------------------------
        go2.apply {
            clear()
            addVertices(9f)
            connect(2, 3)
            connect(3, 2)
            connect(3,70)
            connect(1, 'A', Edbl(4.0))
            connect(3, 'B', Edbl(7.0, "3-B"))
            name = "NewGo2"
            //toGraphviz().show()
        }
        val go3 = gSum - go2

        go3.name="minus"
        //go3.disconnectAll(3,'B')
        //go3.toGraphviz().show()


        go2.clear()
        go2.connect(3, 1.1, edgeDefaultInt)
        go2.connect(3, 'B', Edbl(label = "3-B"))
        go2.connect(1, 'A', Edbl(4.0))
        go2.connect(3, 'B', Edbl(4.0, "3-B"))
        go2.connect(1, 2, Eint(1, "go1"))
        go2.connect(2, 3)

        // assertEquals(go1, go2, "Equals 1")
        go2.addVertices("a")
        go2.disconnect(1, 'A', Edbl(4.0))
        go2.connect(1, 'A', Eint(4))
        // assertNotEquals(go1, go2, "Equals 2")

    }

    //println("$go\n\t---------------------------------\n")
    //val stringVertex = g.filter { (key, value) -> key.endsWith("1") && value > 10}


}