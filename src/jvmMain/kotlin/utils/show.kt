package utils

import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

actual fun String.show( workingDir: String, engine: LayoutEngines): Unit = let { s ->
    val name = s.split("\"".toRegex())[1].replace(" ","")
    File(workingDir).resolve(File("${name}.gv")).bufferedWriter().use { out ->
        out.write(s)
    }

    println("${"cmd /c $engine -Tpng ${name}.gv > ${name}.png".runCommand(File(workingDir))}")
    "cmd /c ${name}.png".runCommand(File(workingDir))
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

enum class OS {
    WINDOWS, LINUX, MAC, SOLARIS
}
/**
 *@see https://www.techiedelight.com/determine-current-operating-system-kotlin/
 * @return OS
 */
fun getOS(): OS? {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    return when {
        os.contains("win") -> {
            OS.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OS.LINUX
        }
        os.contains("mac") -> {
            OS.MAC
        }
        os.contains("sunos") -> {
            OS.SOLARIS
        }
        else -> null
    }
}


