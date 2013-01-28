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

import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
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
    private Charset charset;

    public LanguageMap(String location, boolean inJar, Charset charset) {
        this.location = location;
        this.inJar = inJar;
        this.charset = charset;
    }

    private Reader getReader(Charset charset) throws IOException {
        File file = new File(location);
        InputStream stream;
        if (inJar) {
            stream = getResource(location);
        } else {
            if (!file.exists()) {
                String DEFAULT_LOCATION = "/languages/lang.properties";
                InputStream fromJar = getResource(DEFAULT_LOCATION);
                copy(fromJar, file);
                fromJar.close();
            }
            stream = new FileInputStream(file);
        }

        return new InputStreamReader(stream, charset);
    }

    public static InputStream getResource(String filename) throws IOException {
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

    private Writer getWriter(Charset charset) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(new File(location), true), charset);
    }

    public static void copy(InputStream input, File target) throws IOException {
        if (target.exists()) {
            throw new IOException("File already exists!");
        }

        File parentDir = target.getParentFile();

        if (!parentDir.mkdirs()) {
            throw new IOException("Failed at creating directories!");
        }

        if (!parentDir.isDirectory()) {
            throw new IOException("The parent of this file is no directory!?");
        }

        if (!target.createNewFile()) {
            throw new IOException("Failed at creating new empty file!");
        }

        byte[] buffer = new byte[1024];
        OutputStream output = null;
        try {
            output = new FileOutputStream(target);

            int realLength;

            while ((realLength = input.read(buffer)) > 0) {
                output.write(buffer, 0, realLength);
            }

            output.flush();
            output.close();
        } finally {
            if (output != null) {
                output.flush();
                output.close();
            }
        }
    }

    public void load() {
        Reader reader = null;

        Properties properties;
        try {
            reader = getReader(charset);

            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            Logging.debug(e, false, "Failed at loading the language file!");
            return;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Logging.debug(e, false, "Failed at closing the stream for the language file!");
            }
        }

        map = new ColorizedMap();
        map.importMap(properties);
    }

    public void save() {
        Writer writer = null;
        try {
            writer = getWriter(charset);

            if (!inJar && defaultMap != null) {
                for (Map.Entry<String, String> entry : defaultMap.getEntries()) {
                    if (this.contains(entry.getKey())) {
                        continue;
                    }

                    this.put(entry.getKey(), entry.getValue());

                    writer.write(entry.getKey() + '=' + entry.getValue() + '\n');
                }
            }
        } catch (IOException e) {
            Logging.debug(e, false, "Failed at saving the language file!");
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                Logging.debug(e, false, "Failed at closing the stream for the language file!");
            }
        }
    }

    public LanguageMap getDefaultMap() {
        return defaultMap;
    }

    public void setDefault(LanguageMap defaultMap) {
        this.defaultMap = defaultMap;
    }

    public String put(String key, String value) {
        return map.put(key, ChatBlock.parseColors(value));
    }

    public Set<Map.Entry<String, String>> getEntries() {
        if (map == null) {
            return null;
        }

        return map.entrySet();
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public void clear() {
        if (map == null) {
            return;
        }
        map.clear();
    }

    public String getValue(String key) {
        return map.get(key);
    }
}
