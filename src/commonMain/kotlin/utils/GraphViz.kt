package kgl.utils
import kgl.MutableGraph

enum class LayoutEngines {
    dot, neato, twopi, circo, fdp, osage, patchwork, sfdp
}

/**
 * Generate string compatible with Graphviz DOT Language
 * @see <a href="https://graphviz.org/">Graphviz</a>
 * http://magjac.com/graphviz-visual-editor/
 * https://edotor.net/
 * @return String
 */
fun MutableGraph.toGraphviz(
    labeled: Boolean = true, weighted: Boolean = true, concentrate: Boolean = true
): String {
    val delim = this.directed?.let {
        if (it) '>' else '-'
    } ?: '-'
    val pref = this.directed?.let {
        if (it) "di" else ""
    } ?: ""
    val sb = StringBuilder().apply {
        appendLine("${pref}graph \"${name}\" {")
        appendLine("concentrate=$concentrate")
        if (labeled) {
            appendLine("label = \"$size Vertices, $edgesNumber Edges\"")
            appendLine("edge [fontsize=8,fontcolor=gray40, decorate=true]")
        }
        appendLine("node [color=\"black\", fillcolor = \"white\", style=\"filled,solid\", fontsize=12]")
        vertices.keys.forEach {
            val color = when (it::class.qualifiedName) {
                "kotlin.Boolean" -> "violet"
                "kotlin.Char" -> "yellow2"
                "kotlin.String" -> "yellow"
                "kotlin.Byte" -> "greenyellow"
                "kotlin.Short" -> "green"
                "kotlin.Int" -> "green2"
                "kotlin.Long" -> "green4"
                "kotlin.Float" -> "lightblue"
                "kotlin.Double" -> "lightskyblue"
                "kgraph.MutableGraph" -> "pink, shape = tab"
                else -> "gray"
            }
            appendLine("\"${it}\" [fillcolor = $color]")
        }
        vertices.forEach { v ->
            v.value.forEach { s ->
                append(" \"${v.key}\" -$delim \"${s.first}\"")
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

