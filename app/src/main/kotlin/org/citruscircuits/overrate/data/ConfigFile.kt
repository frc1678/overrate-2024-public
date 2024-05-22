package org.citruscircuits.overrate.data

import android.content.Context
import java.io.File

/**
 * The [File] in which app data is stored.
 */
val Context.configFile get() = File(filesDir, "config.json")
