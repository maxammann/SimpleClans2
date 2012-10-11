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
 *     Last modified: 10.10.12 21:57
 */


package com.p000ison.dev.simpleclans2.language;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Level;

/**
 * Represents a LanguageMap
 */
public class Language {
    private static LanguageMap defaultBundle;
    private static LanguageMap bundle;
    private static final String DEFAULT_FILE_NAME = "lang.properties";

    public static void setInstance(File folder, String language)
    {
        bundle = new LanguageMap(folder, DEFAULT_FILE_NAME, language);

        if (!language.equalsIgnoreCase("default")) {
            defaultBundle = new LanguageMap(folder, DEFAULT_FILE_NAME, "default");

            for (String defaultKey : defaultBundle.keySet()) {
                if (!bundle.containsKey(defaultKey)) {
                    Logging.debug(ChatColor.RED + "The language for the key %s was not found!", Level.WARNING, defaultKey);
                }
            }
        }
    }

    public static String getTranslation(String key, Object... args)
    {
        String bundleOutput = bundle.get(key);

        if (bundleOutput == null) {
            Logging.debug(ChatColor.RED + "The language for the key %s was not found!", Level.WARNING, key);

            if (defaultBundle != null) {

                String defaultBundleOutput = defaultBundle.get(key);
                if (defaultBundleOutput == null) {
                    Logging.debug(ChatColor.RED + "The language for the key %s was not found in the default bundle!", Level.WARNING, key);
                    return "Error!";
                }

                if (args.length > 0) {
                    return MessageFormat.format(defaultBundleOutput, args);
                } else {
                    return defaultBundleOutput;
                }
            }

            //We return a error string because we do not want NPEs!
            return "Error!";
        }

        if (args.length > 0) {
            return MessageFormat.format(bundleOutput, args);
        } else {
            return bundleOutput;
        }
    }

    public static void clear()
    {
        bundle.clear();
        if (defaultBundle != null) {
            defaultBundle.clear();
        }
        bundle = null;
        defaultBundle = null;
    }
}