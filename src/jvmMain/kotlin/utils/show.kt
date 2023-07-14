package utils

import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

actual fun String.show( workingDir: String, engine: LayoutEngines): Unit = let { s ->
    val name = s.split("\"".toRegex())[1].replace(" ","")
    File(workingDir).resolve(File("${name}.gv")).bufferedWriter().use { out ->
        out.write(s)
    }
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
//    println("$ANSI_GREEN $os $ANSI_WHITE")
    val osString: Pair<String, String> = when {
        os.contains("win") -> {
            "cmd /c  $engine -Tpng ${name}.gv > ${name}.png" to
                    "cmd /c ${name}.png"
        }
        os.contains("nix") || os.contains("nux") ||
                os.contains("aix") -> {
            "$engine -Tpng ${name}.gv > ${name}.png" to
                    "gnview ${name}.png &"
        }
        //os.contains("mac") -> ""
        else -> {
            "$engine -Tpng ${name}.gv > ${name}.png" to
                    "${name}.png"
        }
    }
//    println("$ANSI_BLUE $osString $ANSI_WHITE")
    println("${osString.first.runCommand(File(workingDir))}")
    println("${osString.second.runCommand(File(workingDir))}")
}

fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 30,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(this))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
        .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()
