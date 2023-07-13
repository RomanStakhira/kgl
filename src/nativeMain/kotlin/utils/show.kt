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

    platform.posix.system("cd $workingDir")
    platform.posix.system("cmd /c $engine -Tpng ${workingDir}//${name}.gv > ${workingDir}//${name}.png")
    platform.posix.system("cmd /c start ${workingDir}//${name}.png")
}