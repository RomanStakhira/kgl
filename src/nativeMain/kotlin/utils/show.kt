package utils

import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fputs

actual fun String.show(workingDir: String , engine: LayoutEngines): Unit = let { s->
    val name = s.split("\"".toRegex())[1].replace(" ","")
    val file = fopen("${workingDir}//${name}.gv","w") ?:
    throw IllegalArgumentException("Cannot open output file ${workingDir}/${name}.gv")
    try {
        fputs(s, file)
    } finally {
        fclose(file)
    }

    val os = Platform.osFamily.toString().lowercase()
        //println("$ANSI_GREEN $os $ANSI_WHITE")
    val osString: Pair<String, String> = when {
        os.contains("win") -> {
            "cmd /c  $engine -Tpng ${workingDir}//${name}.gv > ${workingDir}//${name}.png" to
                    "cmd /c start ${workingDir}//${name}.png"
        }
        os.contains("nix") || os.contains("nux") ||
                os.contains("aix") -> {
            "$engine -Tpng ${workingDir}//${name}.gv > ${workingDir}//${name}.png" to
                    "gnview ${workingDir}//${name}.png &"
        }
        //os.contains("mac") -> ""
        else -> {
            "$engine -Tpng ${workingDir}//${name}.gv > ${workingDir}//${name}.png" to
                    "${workingDir}//${name}.png"
        }
    }
    //println("$ANSI_BLUE $osString $ANSI_WHITE")
    platform.posix.system(osString.first)
    platform.posix.system(osString.second)
}