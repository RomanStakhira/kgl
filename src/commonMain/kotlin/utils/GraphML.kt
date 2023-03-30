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
    fun head(init: Head.() -> Unit) = initTag(Head(), init)

    fun graph(gname: String? = null, directed: Boolean = true, init: Graphml.() -> Unit) {
        val b = initTag(Graphml(), init)

    }
}

class Head : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
}

class Title : TagWithText("title")

abstract class BodyTag(name: String) : TagWithText(name) {
    fun n(init: N.() -> Unit) = initTag(N(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTag(A(), init)
        a.href = href
    }
}

class Graphml : BodyTag("graph")
class N : BodyTag("node")
class P : BodyTag("p")
class H1 : BodyTag("h1")

class A : BodyTag("a") {
    var href: String
        get() = attributes["href"]!!
        set(value) {
            attributes["href"] = value
        }
}


/**
 * GraphML Builder
 */
fun graphml(init: GraphML.() -> Unit): GraphML {
    val xml = GraphML()
    xml.init()
    return xml
}
//-------------------------------------------------------------------------------
const val preamble = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
fun MutableGraph.toGraphML(
    labeled: Boolean = true, weighted: Boolean = true,
    concentrate: Boolean = true, colorMap: Map<Any, Int>? = null
): String {
    val result = graphml {
//        head {
//            title { +"Title" }
//        }
        graph("Name", false) {
            n{ + "1"}
            a(href = "http://kotlinlang.org") {
                +"Kotlin"
            }

            /*
            h1 { +"HTML encoding with Kotlin" }
            p {
                +"this format can be used as an"
                +"alternative markup to HTML"


                // mixed content
                p {
                    +"This is some"
                    n { +"mixed" }
                    +"text. For more see the"
                    a(href = "http://kotlinlang.org") {
                        +"Kotlin"
                    }
                    +"project"
                }
                p {
                    +"some text"
                }
            }
             */

        }

    }
    return preamble + result.toString()
}