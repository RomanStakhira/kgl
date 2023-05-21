import kgl.interfaceEdge


/**
 * Graph with Edge (weight, label)
 * @property [vertices] Main graph data storage
 * @constructor Creates a new instance of MutableGraph using given input
 * @property [directed] null if Graph is mixed
 * @property [name] Graph name
 */
//@Serializable
class MutableGraph(
    override var name: String = "",
    override var directed: Boolean? = true
) : Graph() {

    override val vertices = mutableMapOf<Any, MutableSet<Pair<Any, interfaceEdge<*>?>>>()

    fun copy(newName: String? = null) =
        MutableGraph(newName ?: "${name}_Copy", directed).also {
            it.vertices += vertices
        }
    /**
     * Clear a graph
     */
    inline fun clear() {
        vertices.clear()
    }

    /**
     * Add a Vertex
     * @param [v] Vertex any object
     */
    private fun <T> addVertex(v: T) {
        vertices[v as Any] = mutableSetOf<Pair<Any, interfaceEdge<*>?>>()
    }
    /**
     * Adds a number of Vertices
     * @param [verts] Vertex objects
     */
    fun <T> addVertices(vararg verts: T) {
        verts.forEach {
            addVertex(it)
        }
    }
    /**
     * Remove Vertex
     * @param [v] Vertex in Graph
     */
    private fun <T> rmVertex(v: T) {
        vertices -= v as Any
        //Remove from all places
        val itrVert = vertices.keys.iterator()
        itrVert.forEach {
            // what needs to be removed from the links
            val setToRemove = mutableSetOf<Pair<Any, interfaceEdge<*>?>>()
            vertices[it]?.forEach { p ->
                if (p.first == v) setToRemove.add(p)
            }
            if (setToRemove.isNotEmpty()) {
                vertices[it]!!.removeAll(setToRemove)
            }
        }
    }
    /**
     * Remove a number of Vertices
     * @param v Vertex objects
     */
    fun <T> rmVertices(vararg v: T) {
        v.forEach {
            rmVertex(it)
        }
    }

    /**
     * Connect two vertices. Create if not exist.
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     * @param [edge] Edge object, default null
     */
    fun <T> connect(start: T, finish: T, edge: interfaceEdge<*>? = null) {
        vertices[start as Any] ?: addVertex(start)
        vertices[finish as Any] ?: addVertex(finish)
        vertices[start]!!.add((finish as Any) to edge)
        if (this.directed == false) {
            vertices[finish]!!.add((start as Any) to edge)
        }
    }

    /**
     * Disconnect two vertices. Does not remove vertices.
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     * @param [edge] Edge object, default null
     */
    fun <T> disconnect(start: T, finish: T, edge: interfaceEdge<*>? = null) {
        vertices[start as Any]?.remove(vertices[start]!!.find { p -> p.first == finish && p.second == edge })
        if (this.directed == false) {
            vertices[finish as Any]?.remove(vertices[finish]!!.find { p -> p.first == start && p.second == edge })
        }
    }

    /**
     * Remove all bonds between two vertices. Does not remove vertices.
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     */
    fun <T> disconnectAll(start: T, finish: T) {
        val r = vertices[start as Any]?.remove(vertices[start as Any]!!.find { p -> p.first == finish })
        if (this.directed == false) {
            vertices[finish as Any]?.remove(vertices[finish as Any]!!.find { p -> p.first == start })
        }
        if (r == true) disconnectAll(start, finish)
    }

    /**
     * Set Edge for vertices in given Graph
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     * @param [edge] present edge
     * @param [newEdge] new edge
     * @return true if edge was set
     */
    fun <T> setEdge(start: T, finish: T, edge: interfaceEdge<*>? = null, newEdge: interfaceEdge<*>? = null): Boolean {
        if (start as Any !in vertices.keys || finish as Any !in vertices.keys) return false
        vertices[start]!!.remove(finish as Any to edge)
        vertices[start]!!.add(finish as Any to newEdge)
        if (this.directed == false) {
            vertices[finish]!!.remove(start as Any to edge)
            vertices[finish]!!.add(start as Any to newEdge)
        }
        return true
    }

    /**
     * Get Edge for a pair of vertices
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     * @return set of Edge objects (Empty set if vertices doesn't connected)
     */
    fun <T> getEdge(start: T, finish: T): Set<interfaceEdge<*>?> {
        //val ed = vertices[start as Any]?.filter { p-> p.first == to as Any }
        val e = mutableSetOf<interfaceEdge<*>?>()
        vertices[start as Any]?.forEach { p ->
            if (p.first == finish as Any) e += p.second
        }
        return e.toSet()
    }

    /**
     * Set Uniform Edges for all vertices in current Graph
     * @param [edge] Edge object
     */
    fun setEdges(edge: interfaceEdge<*>? = null) {
        val itrVert = vertices.keys.iterator()
        itrVert.forEach { v ->
            val mlNeighbors = vertices[v]!!.toMutableList()
            val itrNeighbors = mlNeighbors.listIterator()
            while (itrNeighbors.hasNext()) {
                itrNeighbors.set(itrNeighbors.next().first to edge)
            }
            vertices[v]!!.apply {
                clear()
                addAll(mlNeighbors.toMutableSet())
            }
        }
    }
    /**
     * Adds new neighboring vertices and connect. Edge set null
     * @param [start] start
     * @param [finish] list of neighbors
     */
    fun <T> addNeighbors(start: T, vararg finish : T) {
        finish.forEach {
            connect(start, it)
        }
    }
    /**
     * Adds a series of vertices and connects them to each other
     * @param [start]
     * @param [finish]
     */
    fun <T> addPath(start: T, vararg finish: T) {
        var tmp = start
        finish.forEach {
            connect(tmp, it)
            tmp = it
        }
    }
//------------------ Operators ---------------------
    /**
     * @return Graph += Vertex
     */
    operator fun <T> plusAssign(v: T){
        this.addVertex(v)
    }
    /**
     * @return NewGraph = Graph + Vertex
     */
    operator fun <T> plus(v: T): MutableGraph {
        val tmp = this.copy("plus")
        tmp.addVertex(v)
        return tmp
    }
    /**
     * @return Graph -= Vertex
     */
    operator fun <T> minusAssign(v: T){
        rmVertex(v)
    }
    /**
     * @return NewGraph = Graph - Vertex
     */
    operator fun <T> minus(v: T): MutableGraph {
        val tmp = this.copy("minus")
        tmp.rmVertex(v)
        return tmp
    }

    /**
     * @return new directed property from this & other graphs (null if properties in both graphs are diferent)
     *
     * @param [other] Graph
     */
    private fun newDirected(other : MutableGraph) = this.directed?.let { t ->
        other.directed?.let { o ->
            if (t && o) {
                true
            } else if (!t && !o) {
                false
            } else null
        }
    }
    operator fun plusAssign(other: MutableGraph){
        directed = newDirected(other)
        //putAll  don't work
        //this.vertices.putAll(other.vertices)
        vertices += (vertices.keys union other.vertices.keys).associateWith { v ->
            val s = mutableSetOf<Pair<Any, interfaceEdge<*>?>>().apply {
                addAll(vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, interfaceEdge<*>?>>())
                addAll(other.vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, interfaceEdge<*>?>>())
            }
            s
        }

    }

    /**
     * Create a new graph
     * @param [other] Graph
     * @return a graph containing all vertices and edges from both
     */
    operator fun plus(other: MutableGraph): MutableGraph {
        //putAll  don't work
        val tmp = MutableGraph("${name}${other.name}", newDirected(other))
        tmp.vertices += (this.vertices.keys union other.vertices.keys).associateWith { v ->
            val s = mutableSetOf<Pair<Any, interfaceEdge<*>?>>().apply {
                addAll(vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, interfaceEdge<*>?>>())
                addAll(other.vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, interfaceEdge<*>?>>())
            }
            s
        }
        return tmp
    }





    //-----------------------------------------------------------------------------------------
    operator fun minusAssign(other: MutableGraph){

        // this
    }

    private fun <T> objInBundles(o: T): Boolean {
        vertices.keys.forEach { v ->
            if (vertices[v]!!.find { p-> p.first == o } != null) return true
        }
        return false
    }

    operator fun minus(other: MutableGraph): MutableGraph {
         this.directed?.let { t ->
            other.directed?.let { o ->
                if (t && o) {
                    true
                } else if (!t && !o) {
                    false
                } else null
            }
        }
        val newName: String = name.replace(other.name, "")
        val tmp = this.copy(newName)
        val vertToRemove = mutableSetOf<Any>()
        val itrVert = tmp.vertices.keys.iterator()
        itrVert.forEach { tmpVert ->
            // what needs to be removed from the links
            val setToRemove = mutableSetOf<Pair<Any, interfaceEdge<*>?>>()
            if (tmpVert in other.vertices.keys) {
                tmp.vertices[tmpVert]?.forEach { p ->
                    if (other.vertices[tmpVert]?.contains(p) == true) setToRemove.add(p)
                }
                if (setToRemove.isNotEmpty()) {
                    tmp.vertices[tmpVert]!!.removeAll(setToRemove)
                }
                //remove lonely vertex
                //necessary to check the presence of the vertex in the bundles
                if (tmp.vertices[tmpVert]!!.isEmpty() && !tmp.objInBundles(tmpVert)) {
                    vertToRemove.add(tmpVert)
                }
            }
        }
        tmp.vertices.keys.removeAll(vertToRemove)
        return tmp
    }




}


