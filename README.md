# KGL
Multiplatform Kotlin Graph Library.
Pre-alpha

KGL provides a simply to use platform independent graph library.  It supports directed graphs, undirected graphs, mixed graphs and loops.



## Usage
KGL is made by main interface [Graph](src/commonMain/kotlin/AbstractGraph.kt) and its 
implementation [MutableGraph.kt](src/commonMain/kotlin/MutableGraph.kt).

### Methods:
Methods and their description can be obtained using the dokka plugin.

### Operators:
* Vertex in Graph
* Vertex !in Graph
* Graph += Vertex
* Graph_1 += Graph_2 (if yuo need graph in vertex use addVertices)
* NewGraph = Graph + Vertex
* NewGraph = Graph_1 + Graph_2 (if yuo need graph in vertex use addVertices)
* Graph -= Vertex
* NewGraph = Graph - Vertex

### What's coming?
- [x] Operators
- [] Serialization  
- [] Algorithms
- [] SubGraph
- [] GraphML [Type-safe builders](https://kotlinlang.org/docs/type-safe-builders.html)
- [] Graph DataBase

[//]: # (@/serialization)

## Copyright and license

Code and documentation copyright 2023 the author. 
Code released under the [Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0) license. 

[//]: # (Docs released under [Creative Commons]&#40;docs/LICENSE&#41;.)