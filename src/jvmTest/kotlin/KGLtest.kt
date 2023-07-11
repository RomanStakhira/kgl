import kotlin.test.*
import utils.LayoutEngines
import utils.show
import utils.toGraphViz


class KGLtest {
    val go = MutableGraph("testOriented")
    val gIn = MutableGraph("Inner Graph", directed = false)

    data class DataClass(val x: Int, val y: Int) {
        val z = x + y
    }
    private val Obj = object {
        val x = 1
        override fun toString(): String {
            return "Object $x"
        }
    }
    val c1 = DataClass(1, 2)

    @BeforeTest
    fun init() {
        gIn.addNeighbors("A", "a")
        gIn.connect("B", "a")

        go.apply {
            addVertices("A", "B", "C")
            addVertices("a", "B", 1, 2, 3.14, 9L)
            addNeighbors("B", c1, 7, 8L, 8, "B", "B")
            connect("C", "a", EDbl(1.0, "Ca"))
            addNeighbors(c1, 2, 8L, "A")
            addNeighbors("a", 2, Obj, gIn)
            addNeighbors("A", "a", 9L)
            addNeighbors(gIn, gIn, "a", 1, 2, "B", 1)
            connect("B", 8, EInt(8, "B8"))
        }
    }

    @Test
    fun testGraph() {
        val classUnderTest = MutableGraph()
        assertNotNull(classUnderTest)
        go.connect('z', "B", EInt(22, "char"))
        val goc = go.clone()

        goc.setEdges(edgeDefaultInt)
        go.toGraphViz().show()
        goc.toGraphViz().show(engine = LayoutEngines.twopi)
        gIn.toGraphViz().show()
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
        go.connect(8, "B", EInt(2, "8"))
        assertEquals(neighborTest.minus(8), go.neighbors("B"), "Neighbor Error 2")
    }

    @Test
    fun testGetEdge() {
        val ed = EDbl(1.0, "Double1")
        val ei1 = EInt(1, "Integer1")
        val ei2 = EInt(2, "Integer2")
        go.connect("A", "B")
        go.connect("A", "B", ed)
        go.connect("A", "B", ei1)
        go.connect("A", "B", ei2)
        go.connect("B", "A", EInt(5))
        go.connect("A", "3", EInt(5))
        var edgeTest = setOf(ed, ei1, ei2, null)
        val edgeRes = go.getEdges("A", "B")
        assertEquals(edgeTest, edgeRes, "Incorrect Edge test 1 !")

        // Disconnect
        go.disconnect("A", "B", ed)
        go.disconnect("A", "B", ei1)
        assertEquals(edgeTest.minus(ed).minus(ei1), go.getEdges("A", "B"), "Incorrect Edge test 2 !")
    }

    @Test
    fun testAnalise() {
        val gA = MutableGraph("", null)
        gA.connect(2, 3, EInt(2))
        gA.connect(3, 5, EDbl(2.0))
        assertTrue(gA.weightsPositive!!, "Weights Positive")
        gA.connect(3, 4)
        gA.connect(1, 2, EInt(-11))
        assertFalse(gA.hasNoose, "Has no cycle")
        gA.connect(3, 3, EInt(77, "cycle 33"))
        gA.connect(5, 5)
        assertFalse(gA.weightsPositive!!, "Weights Negative")
        assertTrue(gA.hasNoose, "Has cycle")
        //gA.toGraphviz().show()
        //println("$gA")
    }
}