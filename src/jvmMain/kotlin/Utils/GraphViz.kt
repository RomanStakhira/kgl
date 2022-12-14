package Utils

import java.io.File
import java.util.concurrent.TimeUnit
import kgl.MutableGraph


enum class LayoutEngines {
    dot, neato, twopi, circo, fdp, osage, patchwork, sfdp
}

/**
 * Compatible with https://graphviz.org/
 * http://magjac.com/graphviz-visual-editor/
 * https://edotor.net/
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


fun String.show(
    workingDir: String = "C:\\tmp",
    engine: LayoutEngines = LayoutEngines.dot
): Unit = let { s ->
    val name = s.split("\"".toRegex())[1]
    File(workingDir).resolve(File("${name}.gv")).bufferedWriter().use { out ->
        out.write(s)
    }
    File(workingDir).resolve(File("${name}.cmd")).bufferedWriter().use { out ->
        out.write("${engine} -Tpng ${name}.gv > ${name}.png\n")
        out.write("start  ${name}.png\n")
    }
    "cmd /c ${name}.cmd".runCommand(File(workingDir))
}
fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(this))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
        .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()

