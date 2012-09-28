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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Represents a Language
 */
public class Language {
    private static final Locale defaultLocale = Locale.getDefault();
    private static Locale locale = defaultLocale;
    private static ResourceBundle bundle;
    private static ResourceBundle defaultBundle = ResourceBundle.getBundle("languages.lang", Locale.ENGLISH);

    public static void setInstance(String loc)
    {
        String[] parts = loc.split("[_\\.]");

        if (parts.length == 1) {
            locale = new Locale(parts[0]);
        } else if (parts.length == 2) {
            locale = new Locale(parts[0], parts[1]);
        } else if (parts.length == 3) {
            locale = new Locale(parts[0], parts[1], parts[2]);
        }

        bundle = ResourceBundle.getBundle("languages.lang", locale);

        for (String defaultKey : bundle.keySet()) {
            if (!bundle.containsKey(defaultKey)) {
                Logging.debug(ChatColor.RED + "The language for the key %s was not found!", Level.WARNING, defaultKey);
            }
        }
    }

    public static String getTranslation(String key)
    {
        if (!bundle.containsKey(key)) {
            Logging.debug(ChatColor.RED + "The language for the key %s was not found!", Level.WARNING, key);

            if (defaultBundle.containsKey(key)) {
                Logging.debug(ChatColor.RED + "The language for the key %s was not found in the default bundle!", Level.WARNING, key);
                return GeneralHelper.parseColors(defaultBundle.getString(key));
            }

            //We return a error string because we do not want NPEs!
            return "Error!";
        }

        return GeneralHelper.parseColors(bundle.getString(key));
    }

    public static String getTranslationMessage(String key, Object... args)
    {
        String translation = getTranslation(key);
        return translation == null ? "Error!" : MessageFormat.format(key, args);
    }

    public static void clear()
    {
        ResourceBundle.clearCache();
    }
}