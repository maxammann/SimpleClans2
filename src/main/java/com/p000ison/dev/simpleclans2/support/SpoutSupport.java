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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.listeners.SCPlayerListener;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import static org.getspout.spoutapi.SpoutManager.getOnlinePlayers;
import static org.getspout.spoutapi.SpoutManager.getPlayer;

/**
 * Represents a SpoutSupport
 */
public class SpoutSupport {

    private SimpleClans plugin;
    private static boolean spout = false;

    public SpoutSupport(SimpleClans plugin)
    {
        this.plugin = plugin;

        PluginManager pm = plugin.getServer().getPluginManager();

        Plugin spout = pm.getPlugin("SpoutPlugin");

        if (!(spout instanceof SpoutPlayer)) {
            return;
        }

        Logging.debug("Hooked Spout!");

        SpoutSupport.spout = true;

        pm.registerEvents(new SCPlayerListener(plugin), plugin);
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

        for (SpoutPlayer spoutPlayer : getOnlinePlayers()) {
            ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayerExact(spoutPlayer.getName());

            if (clanPlayer == null) {
                continue;
            }

            Clan clan = clanPlayer.getClan();

            updateCape(spoutPlayer, clan);
        }
    }

    public static void showNotification(Player player, String title, String message, Material material)
    {
        if (!hasSpout()) {
            getSpoutPlayerExact(player).sendNotification(title, message, material);
        } else {
            player.sendMessage(message);
        }
    }

    public static void setTitle(Player player, String title)
    {
        if (!hasSpout()) {
            return;
        }

        getSpoutPlayerExact(player).setTitle(title);
    }

    public void updateCape(SpoutPlayer spoutPlayer, Clan clan)
    {
        if (clan == null || spoutPlayer == null) {
            return;
        }

        String url = clan.getFlags().getClanCapeURL();

        if (url == null) {
            return;
        }

        spoutPlayer.setCape(url);
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
