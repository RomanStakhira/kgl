# KGL
Multiplatform Kotlin Graph Library.

KGL provides a simply to use platform independent graph library.

## Usage
KGL is made by main interface [Graph](src/commonMain/kotlin/Graph.kt) and its 
implementation [MutableGraph.kt](src/commonMain/kotlin/MutableGraph.kt).


### Operators:
* Vertex in Graph
* Vertex !in Graph
* Graph += Vertex
* Graph_1 += Graph_2 (if yuo need graph in vertex use addVertices)
* NewGraph = Graph + Vertex
* NewGraph = Graph_1 + Graph_2 (if yuo need graph in vertex use addVertices)
* Graph -= Vertex
* NewGraph = Graph - Vertex

### Goals
- [x] Operators
- [] Serialization  
- [] Algorithms
- [] SubGraph
- [] GraphML [Type-safe builders](https://kotlinlang.org/docs/type-safe-builders.html)
- [] Graph DataBase

[//]: # (@/serialization)