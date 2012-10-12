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
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a LanguageMap
 */
public class LanguageMap extends HashMap<String, String> {

    private LanguageMap defaultMap;
    private final String location;
    private final boolean inJar;

    public LanguageMap(String location, boolean inJar)
    {
        this.location = location;
        this.inJar = inJar;

    }

    private Reader getReader() throws IOException
    {
        File file = new File(location);
        if (!file.exists()) {
            String DEFAULT_LOCATION = "/languages/lang.properties";
            copy(LanguageMap.class.getResourceAsStream(DEFAULT_LOCATION), file);
        }
        return inJar ? new InputStreamReader(getResource(location)) : new FileReader(file);
    }

    public static InputStream getResource(String filename)
    {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = LanguageMap.class.getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    private Writer getWriter() throws IOException
    {
        return new FileWriter(new File(location), true);
    }

    public static boolean copy(InputStream input, File target) throws IOException
    {
        if (target.exists()) {
            return false;
        }

        if (!target.getParentFile().mkdir()) {
            return false;
        }

        if (!target.createNewFile()) {
            return false;
        }

        byte[] buffer = new byte[1024];

        OutputStream output = new FileOutputStream(target);

        while (input.read(buffer) != -1) {
            output.write(buffer);
        }

        output.flush();
        output.close();
        return true;
    }

    public void load()
    {
        BufferedReader reader = null;
        Writer writer = null;

        try {
            reader = new BufferedReader(getReader());
            String line;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] entry = line.split("=|' '(.*?)");

                if (entry.length != 2) {
                    continue;
                }

                put(entry[0], entry[1]);
            }

            if (!inJar && defaultMap != null) {
                writer = new BufferedWriter(getWriter());

                for (Map.Entry<String, String> entry : defaultMap.entrySet()) {
                    if (this.containsKey(entry.getKey())) {
                        continue;
                    }

                    this.put(entry.getKey(), entry.getValue());

                    writer.write(entry.getKey());
                    writer.write('=');
                    writer.write(entry.getValue());
                    writer.write('\n');
                }
            }
        } catch (IOException e) {
            Logging.debug(e, "Failed at loading the language file!");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                Logging.debug(e, "Failed at closing the stream for the language file!");
            }
        }
    }

    public LanguageMap getDefaultMap()
    {
        return defaultMap;
    }

    public void setDefault(LanguageMap defaultMap)
    {
        this.defaultMap = defaultMap;
    }

    @Override
    public String put(String key, String value)
    {
        return super.put(key, ChatBlock.parseColors(value));
    }
}
