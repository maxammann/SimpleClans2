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


package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a ClanFlags
 */
@SuppressWarnings("unchecked")
public class Flags {

    private Map<String, Object> data = new HashMap<String, Object>();

    public Map<String, Object> getData()
    {
        return data;
    }

    public void write(String jsonString)
    {
        if (jsonString == null || jsonString.isEmpty()) {
            return;
        }

        JSONObject flags = (JSONObject) JSONValue.parse(jsonString);

        if (flags == null) {
            return;
        }

        Set<Map.Entry> entrySet = new HashSet<Map.Entry>(flags.entrySet());

        for (Map.Entry entry : entrySet) {

            Object key = entry.getKey();
            Object value = entry.getValue();

            try {

                Set endValue = null;

                if (value instanceof JSONArray) {
                    endValue = new HashSet();
                    JSONArray list = (JSONArray) value;

                    for (Object element : list) {
                        endValue.add(element.toString());
                    }
                }

                if (endValue != null) {
                    data.put(key.toString(), endValue);
                } else {
                    data.put(key.toString(), value);
                }

            } catch (Exception ex) {
                Logging.debug(ex, true, "Failed at loading the flags");
            }
        }
    }

    public String read()
    {

        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key == null || value == null) {
                continue;
            }

            if (value instanceof Set) {
                Set set = (Set) value;
                if (set.isEmpty()) {
                    continue;
                }
            }

            json.put(key, value);
        }

        return json.toJSONString();
    }

    public boolean hasFlags()
    {
        return data != null && !data.isEmpty();
    }

    public boolean getBoolean(String key)
    {
        Object bool = data.get(key);

        if (bool instanceof Boolean) {
            return (Boolean) bool;
        }

        return false;
    }

    public Set getSet(String key)
    {
        Object set = data.get(key);

        if (set instanceof Set) {
            return (Set) set;
        }

        Set empty = new HashSet();

        setSet(key, empty);

        return (Set) empty;
    }

    public void setSet(String key, Set set)
    {
        data.put(key, set);
    }


    public void setBoolean(String key, boolean bool)
    {
        data.put(key, bool);
    }

    public String getString(String key)
    {
        Object string = data.get(key);

        if (string == null) {
            return null;
        }

        return string.toString();
    }

    public void setString(String key, String value)
    {
        data.put(key, value);
    }
}
