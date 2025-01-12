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
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.eagerOption
import com.github.ajalt.clikt.parameters.options.versionOption
import com.mayekukhisa.scaffold.model.Template
import com.mayekukhisa.scaffold.model.TemplateCatalog
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Path
import java.util.Properties

class App : CliktCommand(name = BuildConfig.NAME) {
  override val printHelpOnEmptyArgs = true

  init {
    versionOption(BuildConfig.VERSION)
    eagerOption("--list-templates", help = "Show available templates and exit") {
      throw PrintMessage(
        if (templates.isEmpty()) {
          "No templates found"
        } else {
          templates.joinToString(System.lineSeparator()) { it.name }
        },
      )
    }

    context {
      helpFormatter = { MordantHelpFormatter(it, requiredOptionMarker = "*", showDefaultValues = true) }
    }
  }

  override fun help(context: Context) = "A project structure generator tool"

  override fun helpEpilog(context: Context) = "Homepage: https://github.com/mayekukhisa/scaffold#readme"

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

    val templates: List<Template> by lazy {
      val templateCatalog =
        config
          .getProperty("template.collection.path")
          ?.let {
            File(it, "catalog.json").apply {
              if (!exists()) {
                FileUtils.writeStringToFile(
                  this,
                  Json.encodeToString(TemplateCatalog(emptyList())),
                  Charsets.UTF_8,
                )
              }
            }
          }
          ?: throw PrintMessage(
            "Error: Template collection path not set",
            statusCode = 1,
            printError = true,
          )

      try {
        with(Json { ignoreUnknownKeys = true }) {
          decodeFromString<TemplateCatalog>(
            FileUtils.readFileToString(templateCatalog, Charsets.UTF_8),
          ).templates
        }
      } catch (e: SerializationException) {
        throw PrintMessage("Error: Invalid template catalog", statusCode = 1, printError = true)
      }
    }
  }
}
