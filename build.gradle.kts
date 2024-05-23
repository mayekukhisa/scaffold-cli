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
plugins {
   application
   alias(libs.plugins.kotlin.jvm)
   alias(libs.plugins.diffplug.spotless)
}

kotlin {
   jvmToolchain(17)
}

spotless {
   with(rootProject.file("spotless/copyright/kotlin.txt")) {
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

   with(rootProject.file("spotless/config/prettierrc.json")) {
      json {
         target("**/*.json")
         targetExclude("**/build/**/*.json")
         prettier().configFile(this@with)
      }

      format("Markdown") {
         target("**/*.md")
         targetExclude("**/build/**/*.md")
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
   testImplementation(libs.kotlin.test)
}

group = "com.mayekukhisa.scaffold"
version = "0.1.0"

application {
   mainClass.set("${project.group}.MainKt")
}

tasks {
   named<Test>("test") {
      useJUnitPlatform()
   }
}
