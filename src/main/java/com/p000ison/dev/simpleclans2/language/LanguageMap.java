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
 *     Created: 10/2/12 6:14 PM
 */

package com.p000ison.dev.simpleclans2.language;

import com.p000ison.dev.simpleclans2.util.Logging;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Represents a LanguageMap
 */
public class LanguageMap extends HashMap<String, String> {

    public LanguageMap(File folder, String file, String language)
    {
        load(folder, file, language);
    }

    private void load(File folder, String file, String language)
    {
        if (!language.equalsIgnoreCase("default")) {
            String[] fullName = file.split("\\.");

            if (fullName.length != 2) {
                Logging.debug(Level.SEVERE, "Invalid file name of the language file!");
                return;
            }

            String name = fullName[0];
            String extension = fullName[1];

            file = name + '_' + language + '.' + extension;
        }

        File realFile = new File(folder, file);

        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(realFile)));
        } catch (FileNotFoundException e) {
            Logging.debug("The language file %s was not found!", Level.SEVERE, file);
            return;
        }

        String line;

        try {
            while ((line = reader.readLine()) != null) {

                String trimmed = line.trim();

                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] entry = line.split("=|' '(.*?)");

                if (entry.length != 2) {
                    continue;
                }

                put(entry[0], entry[1]);
            }
        } catch (IOException e) {
            Logging.debug(e, "Failed at loading the language file %s!", file);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Logging.debug(e, "Failed at closing the stream for the language file %s!", file);
            }
        }
    }
}
