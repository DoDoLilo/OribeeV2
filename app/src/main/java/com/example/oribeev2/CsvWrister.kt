package com.example.oribeev2

import android.util.Log
import java.io.File
import java.io.FileWriter


fun writeToLocalStorage(filePath: String, content: String) {
    val dir = filePath.substring(0, filePath.indexOfLast { it == '/' })
    Log.d("csv","write path dir: $dir")
    val f = File(dir)
    if (!f.exists()) {
        f.mkdirs()
    }
    val file = File(filePath)
    if (!file.exists()) {
        file.createNewFile()
    }
    val out = FileWriter(file)
    out.write(content)
    out.flush()
    out.close()
}
