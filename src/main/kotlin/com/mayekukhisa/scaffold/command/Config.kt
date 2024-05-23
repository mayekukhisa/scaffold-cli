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
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.single
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.eagerOption
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.splitPair
import com.mayekukhisa.scaffold.App

class Config : CliktCommand(
   help = "Manage tool configurations",
   printHelpOnEmptyArgs = true,
) {
   private val action by mutuallyExclusiveOptions(
      option(
         "--set",
         metavar = "key=value",
         help = "Set the value for the specified key",
      ).splitPair().convert { Action.Set(it) },
      option(
         "--get",
         metavar = "key",
         help = "Retrieve the value for the specified key",
      ).convert { Action.Get(it) },
      option(
         "--unset",
         metavar = "key",
         help = "Remove the configuration entry matching the specified key",
      ).convert { Action.Unset(it) },
   ).single()

   init {
      eagerOption("--list", help = "Show all configuration key/value pairs") {
         throw PrintMessage(
            if (App.config.isEmpty) {
               "No configurations found"
            } else {
               App.config.entries.joinToString(System.lineSeparator()) { (key, value) -> "$key=$value" }
            },
         )
      }
   }

   override fun run() {
      when (action) {
         is Action.Set -> {
            App.config.apply {
               val stringPair = (action as Action.Set).stringPair
               setProperty(stringPair.first, stringPair.second)
               App.configFile.outputStream().use { store(it, null) }
            }
         }

         is Action.Get -> {
            echo(App.config.getProperty((action as Action.Get).key) ?: throw ProgramResult(1))
         }

         is Action.Unset -> {
            App.config.apply {
               remove((action as Action.Unset).key)
               App.configFile.outputStream().use { store(it, null) }
            }
         }

         else -> throw ProgramResult(1) // This should never happen though
      }
   }

   private sealed class Action {
      data class Set(val stringPair: Pair<String, String>) : Action()

      data class Get(val key: String) : Action()

      data class Unset(val key: String) : Action()
   }
}
