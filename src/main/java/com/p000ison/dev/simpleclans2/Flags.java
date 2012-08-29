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
                Logging.debug(ex, "Failed at loading the flags");
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
