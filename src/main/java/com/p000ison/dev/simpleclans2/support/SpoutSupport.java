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
 *     Created: 12.09.12 15:33
 */

package com.p000ison.dev.simpleclans2.support;

import com.p000ison.dev.simpleclans2.SimpleClans;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.player.SpoutPlayer;

import static org.getspout.spoutapi.SpoutManager.getOnlinePlayers;
import static org.getspout.spoutapi.SpoutManager.getPlayer;

/**
 * Represents a SpoutSupport
 */
public class SpoutSupport {

    private SimpleClans plugin;
    private static boolean spout;

    public SpoutSupport(SimpleClans plugin)
    {
        this.plugin = plugin;

        Plugin spout = plugin.getServer().getPluginManager().getPlugin("SpoutPlugin");

        if (!(spout instanceof SpoutPlayer)) {
            return;
        }

        SpoutSupport.spout = true;
    }

    public static boolean hasSpout()
    {
        return spout;
    }

    public void updateAllCapes()
    {
        if (!hasSpout()) {
            return;
        }
    }

    public static SpoutPlayer getSpoutPlayerExact(Player player)
    {
        return getPlayer(player);
    }

    public static SpoutPlayer getSpoutPlayerExact(String player)
    {
        for (SpoutPlayer spoutPlayer : getOnlinePlayers()) {
            if (player.equals(spoutPlayer.getName())) {
                return spoutPlayer;
            }
        }

        return null;
    }

    public static SpoutPlayer getSpoutPlayer(String player)
    {
        String clean = player.toLowerCase();
        for (SpoutPlayer spoutPlayer : getOnlinePlayers()) {
            String iClean = spoutPlayer.getName().toLowerCase();
            if (iClean.equals(clean) || iClean.startsWith(clean)) {
                return spoutPlayer;
            }
        }

        return null;
    }
}
