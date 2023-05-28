package utils

actual fun String.show(workingDir: String, engine: LayoutEngines): Unit = let { s->
    println("$s")
}