/**
 * Greedy coloring
 *
 * @param [maxColors] The maximum number of colors for coloring (start from 0) default: Int.MAX_VALUE,
 * @param [seed]  Start vertex
 * @return Map<Any?, Int>(vertex, color) or empty map if [maxColors] has been exceeded
 */
fun AbstractGraph.greedyColoring (maxColors: Int = Int.MAX_VALUE,
                                  seed: Any? = null) : Map<Any, Int>{
    if (hasNoose) throw Exception("Graph has noose")
    val colorMap = mutableMapOf<Any, Int>()
    val queue = mutableSetOf<Any>()
    if (seed in vertices.keys) queue.add(seed!!)

    vertices.forEach {(key, value)->
        if (key !in colorMap) queue.add(key)
        while (queue.isNotEmpty())
            //queue get first & remove
            (queue.first().also { queue.remove(it) }).let{
            // min unused color by neighbors
            for (minColor  in 0..maxColors)
                if ( minColor !in neighbors(it)!!.map {n-> colorMap[n] }){
                    colorMap[it]=minColor
                    break
                } else if (minColor == maxColors) return throw Exception("Too much colors !")
            // neighbors in a queue
            queue.addAll((neighbors(it)!!-colorMap.keys))
        }
    }
    return colorMap.toMap()
}

val AbstractGraph.chromaticNumber: Int
    get(){
        return this.greedyColoring().values.maxOf{ it }+1
    }




