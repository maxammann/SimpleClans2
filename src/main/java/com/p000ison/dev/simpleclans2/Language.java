/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
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