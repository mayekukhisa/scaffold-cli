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
package com.mayekukhisa.scaffold.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.file
import com.mayekukhisa.scaffold.App
import com.mayekukhisa.scaffold.BuildConfig
import com.mayekukhisa.scaffold.model.TemplateFile
import com.mayekukhisa.scaffold.model.TemplateManifest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.StringWriter
import java.util.Locale
import freemarker.template.Configuration as Freemarker

class Create : CliktCommand(
   help = "Generate a new project from a template",
   printHelpOnEmptyArgs = true,
) {
   private val projectTemplate by option(
      "-t",
      "--template",
      metavar = "name",
      help = "The name of the template to use. Use '${BuildConfig.NAME} --list-templates' to see available templates",
   )
      .convert { name -> App.templates.find { it.name == name } ?: fail(name) }
      .required()

   private val projectName by option(
      "-n",
      "--name",
      metavar = "name",
      help = "The name to assign to the project",
   )
      .defaultLazy("project's directory name") { projectDir.name }

   private val packageName by option(
      "-p",
      "--package",
      metavar = "package",
      help = "The base package for the project",
   )
      .default("com.example")
      .validate {
         if (!it.matches(Regex("^[a-z]+(\\.[a-z]+)*$"))) {
            fail("Invalid package")
         }
      }

   private val projectDir by argument(
      name = "directory",
      help = "The directory to create the project in",
   )
      .file()
      .convert { File(it.canonicalPath) }

   private val templateCollectionDir by lazy { File(App.config.getProperty("template.collection.path")) }

   private val freemarker by lazy {
      Freemarker(Freemarker.VERSION_2_3_32).apply {
         setDirectoryForTemplateLoading(templateCollectionDir.resolve(projectTemplate.path))
         defaultEncoding = Charsets.UTF_8.name()
         locale = Locale.US
      }
   }

   override fun run() {
      if (!projectDir.mkdirs()) {
         throw PrintMessage(
            "Error: Failed to create project directory. File exists or permission denied",
            statusCode = 1,
            printError = true,
         )
      }

      try {
         with(Json { ignoreUnknownKeys = true }) {
            val jsonString = freemarker.processTemplateFile("manifest.json")

            decodeFromString<TemplateManifest>(jsonString).run {
               echo("Generating project structure...")

               binaryFiles.forEach { generateProjectFile(it) }
               freemarkerFiles.forEach { generateProjectFile(it, preprocess = true) }
               textFiles.forEach { generateProjectFile(it) }
            }
         }
      } catch (e: IOException) {
         abortProjectGeneration("Error: Template manifest not found")
      } catch (e: SerializationException) {
         abortProjectGeneration("Error: Invalid template manifest")
      }

      echo("Done!")
   }

   private fun Freemarker.processTemplateFile(filepath: String): String {
      val dataModel =
         mapOf(
            "projectName" to projectName,
            "packageName" to packageName,
         )
      return with(StringWriter()) {
         getTemplate(filepath).process(dataModel, this)
         toString()
      }
   }

   private fun generateProjectFile(
      templateFile: TemplateFile,
      preprocess: Boolean = false,
   ) {
      try {
         val outputFile = projectDir.resolve(templateFile.targetPath)

         with(templateCollectionDir.resolve("${projectTemplate.path}/${templateFile.sourceRoot}")) {
            if (preprocess) {
               freemarker.setDirectoryForTemplateLoading(this)
               FileUtils.writeStringToFile(
                  outputFile,
                  freemarker.processTemplateFile(templateFile.sourcePath),
                  Charsets.UTF_8,
               )
            } else {
               FileUtils.copyFile(
                  resolve(templateFile.sourcePath),
                  outputFile,
               )
            }
         }

         outputFile.setExecutable(templateFile.executable, false)
      } catch (e: FileNotFoundException) {
         abortProjectGeneration("Error: Template file '${templateFile.sourcePath}' not found")
      }
   }

   private fun abortProjectGeneration(errorMessage: String) {
      FileUtils.deleteDirectory(projectDir)
      throw PrintMessage(errorMessage, statusCode = 1, printError = true)
   }
}
