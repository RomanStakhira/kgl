/**
 * @property [vertices] Main graph data storage
 * @return
 */
interface InterfaceGraph {
    val name: String
    val directed: Boolean?
    val vertices: Map<Any, Set<Pair<Any, InterfaceEdge<*>?>>>
    operator fun <T> get(v: T) = vertices[v as Any]?.toSet() ?: setOf<Any>()
    /**
     * Neighbors of vertex
     * @param [v] vertex
     * @return Set of neighbors of vertex or null if No such vertex
     */
    fun <T> neighbors(v: T) = vertices[v as Any]?.map { it.first }?.toSet()
    val size: Int
        get() = vertices.size
    val edgesNumber: Int
        get() {
            var result = 0
            vertices.keys.forEach {
                result += vertices[it]!!.size
            }
            // directed?.let {if (!it)  result /=2 }
            return result
        }
}

abstract class AbstractGraph : InterfaceGraph {
    override fun equals(other: Any?) = (other is AbstractGraph && vertices == other.vertices)
    override fun hashCode() = this::class.hashCode()
    /**
     * @return  Human-readable Graph string
     */
    override fun toString(): String {
        val delimiter = this.directed?.let {
            if (it) '>' else '-'
        } ?: ' '
        val sb = StringBuilder().apply {
            appendLine("$name  ($size Vertices, $edgesNumber Edges){")
        }
        vertices.forEach { (key, value) ->
            if (value.isEmpty()) sb.appendLine("$key") // Lonely vertex
            else value.forEach { s ->
                sb.apply {
                    append(" $key -$delimiter ${s.first}")
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
     * Are any vertex connected to itself ?
     */
    val hasNoose: Boolean
        get() {
            vertices.forEach { (key, value)->
                if (key in value.map { it.first }.toSet()) return true
            }
            return false
        }

    /**
     * Are all weights in graph >=0 ?
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

    operator fun contains(v: Any) = v in vertices.keys
}

