package kgl

/**
 * A depth-first search  for a directed or undirected graph
 * @param start
 * @param end
 */
fun MutableGraph.dfs(start: Any, finish: Any) : Number? =
    dfs(this[start], this[finish], setOf())

private fun MutableGraph.dfs(start: Any, finish: Any, visited: Set<Any>): Number? =
    if (start == finish) 0
    else {
        val min = this.neighbors(start)
            ?.filter { it !in visited }
            //?.mapNotNull { dfs(it, finish, visited + start) }
            //.min()
        //if (min == null) null else min + 1
        null
    }
