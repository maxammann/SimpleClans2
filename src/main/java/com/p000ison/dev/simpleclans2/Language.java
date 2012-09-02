/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */



package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Represents a Language
 */
public class Language {
    public static ResourceBundle language;

    public Language()
    {
        language = ResourceBundle.getBundle("languages.lang", Locale.ENGLISH, SimpleClans.class.getClassLoader());
    }

    public static String getTranslation(String key)
    {
        if (!language.containsKey(key)) {
            Logging.debug(ChatColor.RED + "The language for the key %s was not found!", Level.WARNING, key);
            return null;
        }

        return language.getString(key);
    }

    public static void clear()
    {
        ResourceBundle.clearCache(SimpleClans.class.getClassLoader());
        language = null;
    }
}