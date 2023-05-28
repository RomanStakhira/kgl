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

    override val vertices = mutableMapOf<Any, MutableSet<Pair<Any, InterfaceEdge<*>?>>>()

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
    private fun <E> addVertex(v: E) {
        vertices[v as Any] = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>()
    }
    /**
     * Adds a number of Vertices
     * @param [verts] Vertex objects
     */
    fun <E> addVertices(vararg verts: E) {
        verts.forEach {
            addVertex(it)
        }
    }
    /**
     * Remove Vertex
     * @param [v] Vertex in Graph
     */
    private fun <E> rmVertex(v: E) {
        vertices -= v as Any
        //Remove from all places
        val itrVert = vertices.keys.iterator()
        itrVert.forEach {
            // what needs to be removed from the links
            val setToRemove = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>()
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
    fun <E> rmVertices(vararg v: E) {
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
    fun <E> connect(start: E, finish: E, edge: InterfaceEdge<*>? = null) {
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
    fun <E> disconnect(start: E, finish: E, edge: InterfaceEdge<*>? = null) {
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
    fun <E> disconnectAll(start: E, finish: E) {
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
    fun <E> setEdge(start: E, finish: E, edge: InterfaceEdge<*>? = null, newEdge: InterfaceEdge<*>? = null): Boolean {
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
    fun <E> getEdge(start: E, finish: E): Set<InterfaceEdge<*>?> {
        //val ed = vertices[start as Any]?.filter { p-> p.first == to as Any }
        val e = mutableSetOf<InterfaceEdge<*>?>()
        vertices[start as Any]?.forEach { p ->
            if (p.first == finish as Any) e += p.second
        }
        return e.toSet()
    }

    /**
     * Set Uniform Edges for all vertices in current Graph
     * @param [edge] Edge object
     */
    fun setEdges(edge: InterfaceEdge<*>? = null) {
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
    fun <E> addNeighbors(start: E, vararg finish : E) {
        finish.forEach {
            connect(start, it)
        }
    }
    /**
     * Adds a series of vertices and connects them to each other
     * @param [start]
     * @param [finish]
     */
    fun <E> addPath(start: E, vararg finish: E) {
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
    operator fun <E> plusAssign(v: E){
        this.addVertex(v)
    }
    /**
     * @return NewGraph = Graph + Vertex
     */
    operator fun <E> plus(v: E): MutableGraph {
        val tmp = this.copy("plus")
        tmp.addVertex(v)
        return tmp
    }
    /**
     * @return Graph -= Vertex
     */
    operator fun <E> minusAssign(v: E){
        rmVertex(v)
    }
    /**
     * @return NewGraph = Graph - Vertex
     */
    operator fun <E> minus(v: E): MutableGraph {
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
            val s = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>().apply {
                addAll(vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
                addAll(other.vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
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
            val s = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>().apply {
                addAll(vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
                addAll(other.vertices[v as Any]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
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
            val setToRemove = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>()
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


