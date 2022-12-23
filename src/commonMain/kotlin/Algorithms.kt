package kgl

/**
 * A depth-first search  for a directed or undirected graph
 * @param start
 * @param finish
 */
fun  MutableGraph.dijkstra(start: Any, finish: Any? = null){
    // vertex to Pair(lenght,processed)
    val l = mutableMapOf<Any, Pair<Number, Boolean>>(start to Pair(0,true))    //1


    //val min = this.neighbors(start)?.forEach { println("${getEdge(start ,it) }") } // { getEdge(start,it) }
val x = vertices[start]?.minOfOrNull { it.second?.weight!!.toDouble() }
//forEach { println("${it.first} ${it.second?.weight}  ${it.second?.label}") }
    println("${start}--${finish}   ${x}")
}

/**
 * A depth-first search  for a directed or undirected graph
 * @param start
 * @param finish
 */
fun MutableGraph.dfs(start: Any, finish: Any) : Number? =
    dfs(start, finish, setOf())

private fun MutableGraph.dfs(start: Any, finish: Any, visited: Set<Any>): Number? =
    if (start == finish) 0
    else {
        val min = this.neighbors(start)?.filter { it !in visited }
            ?.map {
                println("$it")
                dfs(it, finish, visited + start)
            }
            //.minOfOrNull()

        //if (min == null) null else min + 1
        println("${start}  ${finish}   ${min}")
        1
    }

fun bfs(start: Any, finish: Any): Int {
    val queue = ArrayDeque<Any>()
    queue.add(start)
    val visited = mutableMapOf(start to 0)
    while (queue.isNotEmpty()) {
        /*
        val next = queue.poll()
        val distance = visited[next]!!
        if (next == finish) return distance
        for (neighbor in next.neighbors) {
            if (neighbor in visited) continue
            visited.put(neighbor, distance + 1)
            queue.add(neighbor)
        }

         */
    }
    return -1
}
