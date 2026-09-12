package com.markvoronin.mapspowersaving.shizuku

import rikka.shizuku.Shizuku
import java.lang.reflect.Method

object ShizukuProcessHelper {
    fun newProcess(cmd: Array<String>): Process? {
        return try {
            val method: Method = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            )
            method.isAccessible = true
            method.invoke(null, cmd, null, null) as? Process
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
