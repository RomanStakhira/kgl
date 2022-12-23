package kgl.utils

import java.io.File
import java.util.concurrent.TimeUnit

actual fun String.show( workingDir: String, engine: LayoutEngines): Unit = let { s ->
    val name = s.split("\"".toRegex())[1]
    File(workingDir).resolve(File("${name}.gv")).bufferedWriter().use { out ->
        out.write(s)
    }
    File(workingDir).resolve(File("${name}.cmd")).bufferedWriter().use { out ->
        out.write("${engine} -Tpng ${name}.gv > ${name}.png\n")
        out.write("start  ${name}.png\n")
    }
    "cmd /c ${name}.cmd".runCommand(File(workingDir))
}
fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(this))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
        .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()

