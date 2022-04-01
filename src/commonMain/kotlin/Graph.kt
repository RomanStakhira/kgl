package kgl

interface Graph {
    val vertices: MutableMap<Any, MutableSet<Pair<Any, Edge<*>?>>>

    /**
     * TODO
     *
     * @param [v]
     * @return
     */
    operator fun <T> get(v: T) = vertices[v as Any]?.toSet() ?: setOf<Any>()
    var name: String
    val oriented: Boolean?
    val size: Int
        get() = vertices.size
    val edgesNumber: Int
        get() {
            var res = 0
            vertices.keys.forEach {
                res += vertices[it]!!.size
            }
            return res
        }
}

abstract class AbstractGraph : Graph {

    override fun equals(other: Any?): Boolean {
        if (other !is Graph || vertices != other.vertices) return false
        return true
    }

    /**
     * @return Graph string
     */
    override fun toString(): String {
        val delim = this.oriented?.let {
            if (it) '>' else '-'
        } ?: ' '
        val sb = StringBuilder().apply {
            appendLine("${name}  ($size Vertices, $edgesNumber Edges){")
        }
        vertices.forEach { v ->
            if (v.value.isEmpty()) sb.appendLine("${v.key}") // Lonely vertex
            else v.value.forEach { s ->
                sb.apply {
                    append(" ${v.key} -$delim ${s.first}")
                    s.second?.let {
                        append(" [")
                        append(s.second!!.label?.let { "\"${it}\"" } ?: "")
                        append(s.second!!.weight?.let { "(${it})" } ?: "")
                        append("]")
                    }
                    appendLine()
                }
            }
        }
        sb.appendLine("}")
        return sb.toString()
    }

    /**
     * Clear a graph
     */
    inline fun clear() {
        vertices.clear()
    }

    /**
     * vertex connected to itself
     */
    val hasNoose: Boolean
        get() {
            vertices.keys.forEach {
                if (neighbors(it)?.contains(it) == true) return true
            }
            return false
        }

    /**
     * Are all weights >=0
     */
    val weightsPositive: Boolean
        get() {
            var res = true
            outerLoop@ for (v in vertices.keys) {
                for (p in vertices[v]!!.toSet()) {
                    res = p.second?.weight?.let {
                        it.toDouble() >= 0
                    } ?: false
                    if (!res) break@outerLoop
                }
            }
            return res
        }
    /**
     * Neighbors of vertex
     * @param [v] vertex
     * @return Set of neighbors of vertex or null if No such vertex
     */
    fun <T> neighbors(v: T) = vertices[v as Any]?.map { it.first }?.toSet() // ?: setOf<Any>()
}

