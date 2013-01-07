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


package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.clan.Clan;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;

/**
 * Represents a JSONUtil
 */
@SuppressWarnings("unchecked")
public final class JSONUtil {

    private JSONUtil()
    {
    }

    public static String clansToJSON(Collection<Clan> clans)
    {
        if (clans == null) {
            return null;
        }

        JSONArray array = new JSONArray();

        for (Clan clan : clans) {
            array.add(clan.getID());
        }

        return array.toJSONString();
    }

    public static String collectionToJSON(Collection collection)
    {
        if (collection == null) {
            return null;
        }

        JSONArray array = new JSONArray();

        array.addAll(collection);

        return array.toJSONString();
    }

    public static String mapToJSON(Map map)
    {
        JSONObject JSONMap = new JSONObject();

        JSONMap.putAll(map);

        return JSONMap.toJSONString();
    }

    public static List<String> JSONToStringList(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        List<String> list = new ArrayList<String>();

        for (Object obj : parser) {
            list.add(obj.toString());
        }

        return list;
    }

    public static Map<Integer, Boolean> JSONToPermissionMap(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONObject parser = (JSONObject) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        for (Object obj : parser.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            Integer integer;
            //
            //Keys in json maps are always strings!
            //
            if (entry.getKey() instanceof String) {
                integer = Integer.parseInt((String) entry.getKey());
            } else {
                integer = (Integer) entry.getKey();
            }
            map.put(integer, (Boolean) entry.getValue());
        }


        return map;
    }

    public static Set<String> JSONToStringSet(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        Set<String> set = new HashSet<String>();

        for (Object obj : parser) {
            set.add(obj.toString());
        }

        return set;
    }

    public static List<Long> JSONToLongList(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        List<Long> list = new ArrayList<Long>();

        for (Object obj : parser) {
            list.add((Long) obj);
        }

        return list;
    }

    public static Set<Long> JSONToLongSet(String json)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        Set<Long> set = new HashSet<Long>();

        for (Object obj : parser) {
            set.add((Long) obj);
        }

        return set;
    }

    public static <T> Set<T> JSONToSet(String json, Set<T> set)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        for (Object obj : parser) {
            set.add((T) obj);
        }

        return set;
    }


    public static <T> List<T> JSONToList(String json, List<T> list)
    {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray parser = (JSONArray) JSONValue.parse(json);

        if (parser == null) {
            return null;
        }

        for (Object obj : parser) {
            list.add((T) obj);
        }

        return list;
    }
}
