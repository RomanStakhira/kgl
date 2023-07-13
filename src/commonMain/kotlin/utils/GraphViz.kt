package utils
import AbstractGraph

enum class LayoutEngines {
    dot, neato, twopi, circo, fdp, osage, patchwork, sfdp
}

/**
 * Generate string compatible with Graphviz DOT Language
 * @param [labeled] Show label for Edge, default true.
 * @param [weighted] Show wight for Edge, default true.
 * @param [concentrate] See GraphViz docs.
 * @param [colorMap] Colors for vertices Map<Vertex,Color>, default null.
 * @see <a href="https://graphviz.org/">Graphviz</a>
 * http://magjac.com/graphviz-visual-editor/
 * https://edotor.net/
 * @return String that can be passed to Graphviz
 */
fun AbstractGraph.toGraphViz(
    labeled: Boolean = true, weighted: Boolean = true,
    concentrate: Boolean = true, colorMap: Map<Any, Int>? = null
): String {
    val delimiter = this.directed?.let {
        if (it) '>' else '-'
    } ?: '-'

    // Start the string creation
    val sb = StringBuilder().apply {
        append(directed?.let {if (it) "di" else "" } ?: "")
        appendLine("graph \"${name}\" {")
        appendLine("concentrate=$concentrate")
        if (labeled) {
            appendLine("label = \"$size Vertices, $edgesNumber Edges\"")
            appendLine("edge [fontsize=8,fontcolor=gray40, decorate=true]")
        }
        appendLine("node [color=\"black\", fillcolor = \"white\", style=\"filled,solid\", fontsize=12]")
        // colorMap not null
        colorMap?.let {c->
            vertices.keys.forEach {v->
                    val color = c[v]?.let {
                    if (it< colors.size) colors[it]// too much colors
                        else colors.last()
                } ?: colors.last()
                appendLine("\"${v}\" [fillcolor = $color]")
            }
        } ?: vertices.keys.forEach {
            val color = when (it::class.simpleName) {
                "Boolean" -> "violet"
                "Char" -> "yellow2"
                "String" -> "yellow"
                "Byte" -> "greenyellow"
                "Short" -> "green"
                "Int" -> "green2"
                "Long" -> "green4"
                "Float" -> "lightblue"
                "Double" -> "lightskyblue"
                "MutableGraph" -> "pink, shape = tab"
                else -> "gray"
            }
            appendLine("\"${it}\" [fillcolor = $color]")
        }
        vertices.forEach { (key, value) ->
            value.forEach { s ->
                append(" \"${key}\" -$delimiter \"${s.first}\"")
                s.second?.let {
                    append(" [")
                    if (labeled) {
                        append(s.second!!.label?.let { "label=\"${it}\", " } ?: "")
                    }
                    if (weighted) {
                        append(s.second!!.weight?.let { "xlabel=\"(${it})\", weight=$it" } ?: "")
                    }
                    append("]")
                }
                appendLine()
            }
        }
        appendLine("}")
    }
    return sb.toString()
}

