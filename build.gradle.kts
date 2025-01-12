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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   application
   alias(libs.plugins.kotlin.jvm)
   alias(libs.plugins.kotlin.serialization)
   alias(libs.plugins.diffplug.spotless)
}

kotlin {
   jvmToolchain(17)
}

spotless {
   with(rootProject.file("spotless/kotlin-header.txt")) {
      kotlin {
         target("**/*.kt")
         targetExclude("**/build/**/*.kt")
         ktlint()
         licenseHeaderFile(this@with).updateYearWithLatest(true)
      }

      kotlinGradle {
         target("**/*.kts")
         targetExclude("**/build/**/*.kts")
         ktlint()
         licenseHeaderFile(this@with, "^(?![\\/ ]\\*).").updateYearWithLatest(true)
      }
   }

   with(rootProject.file("spotless/prettier-config.json")) {
      json {
         target("**/*.json")
         targetExclude("**/build/**/*.json")
         prettier().configFile(this@with)
      }

      format("Markdown") {
         target("**/*.md")
         targetExclude("/CHANGELOG.md", "**/build/**/*.md")
         prettier().configFile(this@with)
      }

      format("Yaml") {
         target("**/*.yml")
         targetExclude("**/build/**/*.yml")
         prettier().configFile(this@with)
      }
   }
}

repositories {
   mavenCentral()
}

dependencies {
   implementation(libs.clikt)
   implementation(libs.commons.io)
   implementation(libs.freemarker)
   implementation(libs.kotlinx.serialization.json)
   testImplementation(libs.kotlin.test)
}

group = "com.mayekukhisa.scaffold"
version = rootProject.file("version.txt").readText().trim()

application {
   mainClass.set("${project.group}.MainKt")
}

val generatedSrcDir = layout.buildDirectory.dir("generated/src")

sourceSets {
   main {
      kotlin.srcDir(generatedSrcDir)
   }
}

distributions {
   main {
      contents {
         from(".") {
            include("LICENSE", "NOTICE", "version.txt")
         }
      }
   }
}

val generateBuildConfig =
   tasks.register("generateBuildConfig") {
      doLast {
         generatedSrcDir.map { it.file("${project.group}/BuildConfig.kt") }.get().asFile.apply {
            parentFile.mkdirs()
            writeText(
               """
               package ${project.group}

               object BuildConfig {
                 const val NAME = "${project.name}"
                 const val VERSION = "${project.version}"
               }
               """.trimIndent(),
            )
         }
      }
   }

tasks {
   named<Test>("test") {
      useJUnitPlatform()
   }

   named<KotlinCompile>("compileKotlin") {
      dependsOn(generateBuildConfig)
   }

   named<Tar>("distTar") {
      compression = Compression.GZIP
      archiveExtension.set("tar.gz")
   }
}
