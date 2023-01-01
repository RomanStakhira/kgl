package kgl

interface Graph {
    val vertices: MutableMap<Any, MutableSet<Pair<Any, interfaceEdge<*>?>>>

    /**
     * TODO
     *
     * @param [v]
     * @return
     */
    operator fun <T> get(v: T) = vertices[v as Any]?.toSet() ?: setOf<Any>()
    var name: String
    val directed: Boolean?
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
        val delim = this.directed?.let {
            if (it) '>' else '-'
        } ?: ' '
        val sb = StringBuilder().apply {
            appendLine("${name}  ($size Vertices, $edgesNumber Edges){")
        }
        vertices.forEach { (key, value) ->
            if (value.isEmpty()) sb.appendLine("${key}") // Lonely vertex
            else value.forEach { s ->
                sb.apply {
                    append(" ${key} -$delim ${s.first}")
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
     * Are all weights in graph >=0
     */
    val weightsPositive: Boolean
        get() {
            var allPositive = true
            outerLoop@ for (v in vertices.keys) {
                val edges = vertices[v]
                if (edges!!.isNotEmpty()){
                    for (e in edges) {
                        allPositive = e.second?.weight?.let {
                            it.toDouble() >= 0
                        } ?: false
                        if (!allPositive) break@outerLoop
                    }
                }
            }
            return allPositive
        }
    /**
     * Neighbors of vertex
     * @param [v] vertex
     * @return Set of neighbors of vertex or null if No such vertex
     */
    fun <T> neighbors(v: T) = vertices[v as Any]?.map { it.first }?.toSet()  //?: setOf<Any>()
}

