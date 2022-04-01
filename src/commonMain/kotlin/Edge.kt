package kgl

interface  Edge <T:Number> {
    val weight: T?
    val label: String?
}


data class EdgeDouble ( override val weight: Double? = null, override val label: String? = null) : Edge<Double> {}
typealias Edbl = EdgeDouble
data class EdgeInteger ( override val weight: Int? = null, override val label: String? = null) : Edge<Int>{}
typealias Eint = EdgeInteger

val edgeDefaultInt : Edge<Int> = object  : Edge<Int> {
    override val weight: Int = 1
    override val label: String? = null
    override fun toString() = weight.toString()
}
