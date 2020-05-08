/*
 * ArgumentParse - Parsing CLI arguments in Java.
 * Copyright © 2020 xaanit (shadowjacob1@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package it.xaan.ap.common.parsing.options;

/**
 * Represents what to do when there are missing arguments.
 */
public enum MissingArgsStrategy {
  /**
   * Immediately exit on a missing argument, this saves processing time but if there
   * are a lot of missing arguments, it can take awhile to tell the user about them all.
   */
  QUICK,
  /**
   * Processes all available arguments before reporting missing ones. This takes more
   * processing time but will tell the user immediately everything they are missing.
   */
  TOTAL
}
