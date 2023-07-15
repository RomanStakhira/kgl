import kotlinx.browser.document
import utils.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestJs {
    @Test
    fun testJs(){
        //document.bgColor="red"

        val g = MutableGraph(name = "Test Graph", directed = true)
        g.apply {
            addVertices("STR", 1, 'b')
            connect(start = "STR", finish = 8, edge = Edge(weight = 1, label = "LABEL"))
            toGraphViz().show(engine = LayoutEngines.twopi)
        }
        assertEquals(4, g.size)

        //document.bgColor="blue"
    }
}