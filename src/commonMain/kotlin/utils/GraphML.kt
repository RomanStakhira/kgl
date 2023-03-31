package kgl.utils
import kgl.MutableGraph


interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

@DslMarker
annotation class GraphMlTagMarker

@GraphMlTagMarker
abstract class Tag(val name: String) : Element {
    val children = arrayListOf<Element>()
    val attributes = hashMapOf<String, String>()

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent<$name${renderAttributes()}>\n")
        for (c in children) {
            c.render(builder, indent + "  ")
        }
        builder.append("$indent</$name>\n")
    }

    private fun renderAttributes(): String {
        val builder = StringBuilder()
        for ((attr, value) in attributes) {
            builder.append(" $attr=\"$value\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

class GraphML : TagWithText("graphml") {
    //fun head(init: Head.() -> Unit) = initTag(Head(), init)

    fun graph(id: String, edgedefault: String, init: GraphTag.() -> Unit) {
        val g = initTag(GraphTag(), init)
        g.id = id
        g.edgedefault = edgedefault

    }
}
/*
class Head : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
}

class Title : TagWithText("title")
 */

abstract class BodyTag(name: String) : TagWithText(name) {
   fun node(id: String, init: N.() -> Unit) {
       val a = initTag(N(), init)
       a.id = id
   }
    fun edge(id: String, source: String, target: String, init: E.() -> Unit) {
        val e = initTag(E(), init)
        e.id = id
        e.source = source
        e.target = target
    }
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)

}

class GraphTag : BodyTag("graph") {  //graph tag
    var id: String
        get() = attributes[id]!!
        set(value) {
            attributes["id"] = value
        }
    var edgedefault: String
        get() = attributes[edgedefault]!!
        set(value) {
            attributes["edgedefault"] = value
        }
}
class N : BodyTag("node"){          //node tag
    var id: String
        get() = attributes[id]!!
        set(value) {
            attributes["id"] = value
        }
}
class E : BodyTag("edge"){  //edge tag
    var id: String
        get() = attributes[id]!!
        set(value) {
            attributes["id"] = value
        }
    var source: String
        get() = attributes[source]!!
        set(value) {
            attributes["source"] = value
        }
    var target: String
        get() = attributes[target]!!
        set(value) {
            attributes["target"] = value
        }
}


class H1 : BodyTag("h1")


/**
 * GraphML Builder
 * @param [{}] lambda function
 */
fun graphml(init: GraphML.() -> Unit): GraphML {
    val xml = GraphML()
    xml.init()
    return xml
}
//-------------------------------------------------------------------------------
const val preamble = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
fun MutableGraph.toGraphML(
    labeled: Boolean = true, weighted: Boolean = true,
    colorMap: Map<Any, Int>? = null
): String {
    val result = graphml {
        graph("Name", "directed") {
            node("iii"){ + "1"}
            node("2"){ }
            edge("8", "iii", "2"){}
            node("3"){}

        }

    }
    return preamble + result.toString()
}