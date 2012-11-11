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

import java.util.*;

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

    public void deserialize(String jsonString)
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

                Object endValue = null;

                if (value instanceof JSONArray) {
                    endValue = new HashSet();
                    JSONArray list = (JSONArray) value;
                    Set endValueSet = (Set) endValue;

                    for (Object element : list) {
                        endValueSet.add(element.toString());
                    }
                } else if (value instanceof Flagable) {
                    endValue = ((Flagable) value).serialize();
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

    public String serialize()
    {
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key == null || value == null) {
                continue;
            }

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                if (collection.isEmpty()) {
                    continue;
                }
                JSONArray list = new JSONArray();
                list.addAll(collection);
                value = list;

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


    public double getDouble(String key)
    {
        Object doubl = data.get(key);

        if (doubl instanceof Double) {
            return (Double) doubl;
        }

        return -1;
    }

    public boolean removeEntry(String key)
    {
        return data.remove(key) != null;
    }

    public Set getSet(String key)
    {
        Object set = data.get(key);

        if (set instanceof Set) {
            return (Set) set;
        }

        Set empty = new HashSet();

        set(key, empty);

        return empty;
    }

    public Set<String> getStringSet(String key)
    {
        Object set = data.get(key);

        if (set instanceof Set) {
            return (Set) set;
        }

        Set<String> empty = new HashSet<String>();

        set(key, empty);

        return empty;
    }

    public void set(String key, Object obj)
    {
        data.put(key, obj);
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

    public void setFlag(String key, Flagable flag)
    {
        data.put(key, flag);
    }

    public Flagable getFlag(String key)

    {
        Object value = data.get(key);

        if (value instanceof Flagable) {
            return (Flagable) value;
        }

        return null;
    }
}
