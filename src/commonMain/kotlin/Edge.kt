interface  InterfaceEdge <T:Number> {
    val weight: T?
    val label: String?
}

data class Edge <T: Number >(override val weight:  T? = null,
                             override val label: String? = null) : InterfaceEdge<T>

data class EdgeDouble ( override val weight: Double? = null,
                        override val label: String? = null) : InterfaceEdge<Double>
typealias EDbl = EdgeDouble
data class EdgeInteger ( override val weight: Int? = null,
                         override val label: String? = null) : InterfaceEdge<Int>
typealias EInt = EdgeInteger

val edgeDefaultInt  = object  : InterfaceEdge<Int> {
    override val weight: Int = 1
    override val label: String? = null
    override fun toString() = weight.toString()
}

