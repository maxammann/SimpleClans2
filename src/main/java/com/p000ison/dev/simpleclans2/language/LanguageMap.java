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
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Represents a LanguageMap
 */
public class LanguageMap {

    private LanguageMap defaultMap;
    private final String location;
    private final boolean inJar;
    private ColorizedMap map;

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

    public static InputStream getResource(String filename) throws IOException
    {
        if (filename == null) {
            throw new IllegalArgumentException("The path can not be null");
        }

        URL url = LanguageMap.class.getResource(filename);

        if (url == null) {
            return null;
        }

        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }

    private Writer getWriter() throws IOException
    {
        return new FileWriter(new File(location), true);
    }

    public static boolean copy(InputStream input, File target) throws IOException
    {
        if (target.exists() || target.isDirectory()) {
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
        Reader reader = null;

        try {
            reader = getReader();

            Properties properties = new Properties();
            properties.load(getReader());
            map = new ColorizedMap();
            map.importMap(properties);
        } catch (IOException e) {
            Logging.debug(e, "Failed at loading the language file!");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Logging.debug(e, "Failed at closing the stream for the language file!");
            }
        }
    }

    public void save()
    {
        Writer writer = null;
        try {
            writer = getWriter();

            if (!inJar && defaultMap != null) {
                writer = new BufferedWriter(getWriter());

                for (Map.Entry<String, String> entry : defaultMap.getEntries()) {
                    if (this.contains(entry.getKey())) {
                        continue;
                    }

                    this.put(entry.getKey(), entry.getValue());

                    writer.write(entry.getKey() + '=' + entry.getValue() + '\n');
                }
            }
        } catch (IOException e) {
            Logging.debug(e, "Failed at saving the language file!");
        } finally {
            try {
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

    public String put(String key, String value)
    {
        return map.put(key, ChatBlock.parseColors(value));
    }

    public Set<Map.Entry<String, String>> getEntries()
    {
        return map.entrySet();
    }

    public boolean contains(String key)
    {
        return map.containsKey(key);
    }

    public void clear()
    {
        map.clear();
    }

    public String getValue(String key)
    {
        return map.get(key);
    }
}
