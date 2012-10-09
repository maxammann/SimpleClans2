/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 08.10.12 18:20
 */

package com.p000ison.dev.simpleclans2.util;

/**
 * Represents a StringHelper
 */
public class StringHelper {

    public static void capitalize(StringBuilder text)
    {
        if (text.length() == 0) {
            return;
        }

        text.setCharAt(0, Character.toUpperCase(text.charAt(0)));
    }

    public static String capitalize(String text)
    {
        StringBuilder builder = new StringBuilder(text);
        capitalize(builder);
        return builder.toString();
    }
}
