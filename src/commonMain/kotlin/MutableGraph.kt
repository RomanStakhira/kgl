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
    override val directed: Boolean? = true
) : Graph() {

    override val vertices = mutableMapOf<Any, MutableSet<Pair<Any, InterfaceEdge<*>?>>>()

    /**
     * Create a clone for Graph  with same content & new name
     * @param  [newName] clone name
     */
    fun clone(newName: String? = null) =
        MutableGraph(newName ?: "${name}_Clone", this.directed).also {
            this.vertices.keys.forEach {key->
                it.addVertex(key)
                this.vertices[key]?.forEach {pair ->
                    it.connect(key, pair.first, pair.second)
                }
            }
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
    private inline fun <E> addVertex(v: E) {
        vertices[v as Any] = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>()
    }
    /**
     * Adds a number of Vertices
     * @param [vs] Vertex objects
     */
    fun <E> addVertices(vararg vs: E) {
        vs.forEach {
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
        vertices.keys.forEach {
            // what needs to be removed from the links
            vertices[it]!!.removeAll {p->
                p.first == v
            }
        }
    }
    /**
     * Remove a number of Vertices
     * @param [vs] Vertex objects
     */
    fun <E> rmVertices(vararg vs: E) {
        vs.forEach {
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
     * Get Edges for a pair of vertices
     * @param [start] 1st vertex
     * @param [finish] 2nd vertex
     * @return set of Edge objects (Empty set if vertices doesn't be connected)
     */
    fun <E> getEdges(start: E, finish: E): Set<InterfaceEdge<*>?> {
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
     * Adds new neighboring vertices and connect. Edge set null.
     * @param [start] start
     * @param [finish] list of neighbors
     */
    fun <E> addNeighbors(start: E, vararg finish : E) {
        finish.forEach {
            connect(start, it)
        }
    }
    /**
     * Adds a series of vertices and connects them to each other. Edge set null.
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
        val tmp = this.clone("")
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
        val tmp = this.clone("")
        tmp.rmVertex(v)
        return tmp
    }
//
    /**
     * @return new directed property from this & other graphs (null if properties in both graphs are different)
     * @param [other] Graph
     */
    private inline fun newDirected(other : MutableGraph): Boolean?  = this.directed?.let { t ->
        other.directed?.let { o ->
            if (o != t) null else o && t
        }
    }
    operator fun plusAssign(other: MutableGraph){
        if (this.directed == other.directed) {
            //putAll  don't work    this.vertices.putAll(other.vertices)
            (vertices.keys union other.vertices.keys).associateWithTo(this.vertices) { v ->
                val s = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>().apply {
                    addAll(vertices[v]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
                    addAll(other.vertices[v]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
                }
                s
            }
        } else throw IllegalArgumentException("Both Graphs must have a same directed property." +
                "Instead use g = g1 + g2")
    }

    /**
     * Create a new graph
     * @param [other] Graph
     * @return a graph containing all vertices and edges from both
     */
    operator fun plus(other: MutableGraph) = MutableGraph("${name}${other.name}", newDirected(other)).also {
        //putAll  don't work
        (this.vertices.keys union other.vertices.keys).associateWithTo(it.vertices) { v ->
            val s = mutableSetOf<Pair<Any, InterfaceEdge<*>?>>().apply {
                addAll(vertices[v]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
                addAll(other.vertices[v]?.toMutableSet() ?: mutableSetOf<Pair<Any, InterfaceEdge<*>?>>())
            }
            s
        }
    }


    //-----------------------------------------------------------------------------------------
    operator fun minusAssign(other: MutableGraph){
        if (this.directed == other.directed) {
            val toProcess = (this.vertices.keys intersect  other.vertices.keys)
           // this.vertices.keys.associateBy

        } else throw IllegalArgumentException("Both Graphs must have a same directed property." +
                "Instead use g = g1 - g2")
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
        val tmp = this.clone(newName)
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


