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
 *     Last modified: 08.01.13 18:58
 */

package com.p000ison.dev.simpleclans2.test;

import com.p000ison.dev.simpleclans2.api.events.ClanCreateEvent;
import com.p000ison.dev.simpleclans2.api.events.ClanPlayerCreateEvent;
import com.p000ison.dev.simpleclans2.api.events.ClanRelationBreakEvent;
import com.p000ison.dev.simpleclans2.api.events.ClanRelationCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a SimpleClansTest
 */
public class SimpleClansTest extends JavaPlugin implements Listener {

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void claims()
    {

    }

    @EventHandler
    private void onClanCreate(ClanCreateEvent event)
    {
        System.out.println(event.getEventName());
    }

    @EventHandler
    private void onClanPlayerCreate(ClanPlayerCreateEvent event)
    {
        System.out.println(event.getEventName());
    }

    @EventHandler
    private void onClanRelationBreak(ClanRelationBreakEvent event)
    {
        System.out.println(event.getEventName());
    }

    @EventHandler
    private void onClanRelationBreak(ClanRelationCreateEvent event)
    {
        System.out.println(event.getEventName());
    }

    @SuppressWarnings("unchecked")
    private static void printPermissionsOfPluginConfig(String path) {
        Yaml yaml = new Yaml(new SafeConstructor());
        Map map = null;
        try {
            map = (Map) yaml.load(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (map != null) {
            Map<String, Object> permissions = (Map<String, Object>) map.get("permissions");

            List<String> output = new ArrayList<String>();
            for (Map.Entry<?, ?> entry : permissions.entrySet()) {
                output.add(entry.getKey().toString() + ": " + ((Map) entry.getValue()).get("description").toString());
            }

            Collections.sort(output);

            for (String d : output) {
                System.out.println(d);
            }
        }
    }
}
