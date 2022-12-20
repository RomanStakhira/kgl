package kgl

/**
 * A depth-first search  for a directed or undirected graph
 * @param start
 * @param end
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
