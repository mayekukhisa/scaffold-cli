/*
 * Copyright 2024 Mayeku Khisa.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.mayekukhisa.scaffold

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.versionOption
import java.io.File
import java.nio.file.Path
import java.util.Properties

class App : CliktCommand(
   name = BuildConfig.NAME,
   help = "A project structure generator tool",
   epilog = "Homepage: https://github.com/mayekukhisa/scaffold#readme",
   printHelpOnEmptyArgs = true,
) {
   init {
      versionOption(BuildConfig.VERSION)
   }

   override fun run() = Unit

   companion object {
      val configFile: File by lazy {
         val osName = System.getProperty("os.name").lowercase()
         val userHome = System.getProperty("user.home")

         val configPath =
            when {
               "win" in osName -> Path.of(userHome, "AppData", "Local", BuildConfig.NAME)
               "mac" in osName -> Path.of(userHome, "Library", "Application Support", BuildConfig.NAME)
               else -> Path.of(userHome, ".config", BuildConfig.NAME)
            }

         configPath.resolve("config.properties").toFile().apply {
            if (!exists()) {
               parentFile.mkdirs()
               createNewFile()
            }
         }
      }

      val config = Properties().apply { configFile.inputStream().use(::load) }
   }
}
