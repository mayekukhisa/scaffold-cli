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

import com.mayekukhisa.scaffold.App
import com.mayekukhisa.scaffold.BuildConfig
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTest {
   private val stdOut = System.out
   private val testStdOut = ByteArrayOutputStream()

   private val tempDir =
      FileUtils.getTempDirectory()
         .resolve("${BuildConfig.NAME}-test")
         .also { it.mkdir() }

   @BeforeTest
   fun setUp() {
      System.setOut(PrintStream(testStdOut))
   }

   @AfterTest
   fun tearDown() {
      System.setOut(stdOut)
      FileUtils.cleanDirectory(tempDir)
   }

   @Test
   fun `should create blank project`() {
      val projectDir = tempDir.resolve("blank-project")
      Create().parse(arrayOf("--template", "blank", "$projectDir"))
      checkProjectStructure(projectDir)
   }

   @Test
   fun `should create android project`() {
      val projectDir = tempDir.resolve("android-project")
      Create().parse(arrayOf("--template", "android", "$projectDir"))
      checkProjectStructure(projectDir)
   }

   @Test
   fun `should create cli project`() {
      val projectDir = tempDir.resolve("cli-project")
      Create().parse(arrayOf("--template", "cli", "$projectDir"))
      checkProjectStructure(projectDir)
   }

   @Test
   fun `should create next project`() {
      val projectDir = tempDir.resolve("next-project")
      Create().parse(arrayOf("--template", "next", "$projectDir"))
      checkProjectStructure(projectDir)
   }

   private fun checkProjectStructure(projectDir: File) {
      val bashPath = App.config.getProperty("bash.path") ?: error("Error: Bash path not set")

      val bashProcess =
         Runtime.getRuntime().exec(bashPath, null, projectDir).apply {
            val command = "find . -type f -exec stat -c \"%a %n\" {} \\; | LC_ALL=C sort"
            outputStream.use { IOUtils.write(command, it, Charsets.UTF_8) }
            waitFor()
         }

      val expectedStructure =
         IOUtils.resourceToString("/project-structures/${projectDir.name}.txt", Charsets.UTF_8)
            .replace("\r\n", "\n")
      assertEquals(expectedStructure, IOUtils.toString(bashProcess.inputStream, Charsets.UTF_8))
   }
}
