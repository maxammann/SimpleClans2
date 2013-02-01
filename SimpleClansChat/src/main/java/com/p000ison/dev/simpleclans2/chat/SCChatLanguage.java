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
 *     Last modified: 1/28/13 8:55 PM
 */

package com.p000ison.dev.simpleclans2.chat;

import com.p000ison.dev.simpleclans2.api.util.LanguageMap;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Represents a SCChatLanguage
 */
public class SCChatLanguage {
    private static LanguageMap bundle;
    private static final String DEFAULT_FILE_NAME = "lang.properties";

    public static void setInstance(File folder, Charset charset) {
        LanguageMap defaultBundle = new LanguageMap("/languages/" + DEFAULT_FILE_NAME, true, charset, SCChatLanguage.class);
        defaultBundle.load();
        bundle = new LanguageMap(new File(folder, DEFAULT_FILE_NAME).getAbsolutePath(), false, charset, SCChatLanguage.class);
        bundle.setDefault(defaultBundle);
        bundle.load();
        bundle.save();
    }

    public static String getTranslation(String key, Object... args) {
        return bundle.getTranslation(key, args);
    }

    public static void clear() {
        bundle.clear();
        bundle = null;
    }
}
