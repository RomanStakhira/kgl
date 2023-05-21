package kgl

interface  interfaceEdge <T:Number> {
    val weight: T?
    val label: String?
}

data class Edge <T: Number >(override val weight:  T? = null,
                             override val label: String? = null) : interfaceEdge<T> {}

data class EdgeDouble ( override val weight: Double? = null,
                        override val label: String? = null) : interfaceEdge<Double> {}
typealias Edbl = EdgeDouble
data class EdgeInteger ( override val weight: Int? = null,
                         override val label: String? = null) : interfaceEdge<Int>{}
typealias Eint = EdgeInteger

val edgeDefaultInt  = object  : interfaceEdge<Int> {
    override val weight: Int = 1
    override val label: String? = null
    override fun toString() = weight.toString()
}

