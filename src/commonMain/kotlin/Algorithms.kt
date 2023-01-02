package kgl

import kotlin.properties.Delegates

/**
 * A Dijkstra search  for a directed or undirected graph
 * @param [start]
 * @param [finish]
 * @return
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
 * @param [start]
 * @param [finish]
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
        //println("${start}  ${finish}   ${min}")
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

/**
 * Greedy coloring
 *
 * @param [maxColors] The maximum number of colors for coloring (start from 0) default: Int.MAX_VALUE,
 * @param [seed]  Start vertex
 * @return Map<Any?, Int>(vertex, color) or empty map if [maxColors] has been exceeded
 */
fun Graph.greedyColoring (maxColors: Int = Int.MAX_VALUE,
                                 seed: Any? = null) : Map<Any?, Int>{
    val colorMap = mutableMapOf<Any?, Int>()
    val queue = ArrayDeque<Any?>()
    if (seed in vertices.keys) queue.add(seed)

    vertices.forEach {(key, value)->
        if (key !in colorMap) queue.add(key)
        while (queue.isNotEmpty())
            queue.removeFirstOrNull().let{
            // min unused color by neighbors
            for (c  in 0..maxColors)
                if ( c !in neighbors(it)!!.map {n-> colorMap[n] }){
                    colorMap[it]=c
                    break
                } else if (c==maxColors) return mapOf<Any?, Int>()
            // neighbors in a queue
            queue.apply {
                addAll((neighbors(it)!!-colorMap.keys))
                val q = queue.toSet()                    //distinct
                clear()
                addAll(q)
            }
        }
    }
    return colorMap.toMap()
}





