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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.teleportation;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles teleporting with cooldown
 */
public class TeleportManager {
    private SimpleClans plugin;
    private Set<TeleportState> waitingPlayers = new HashSet<TeleportState>();

    /**
     *
     */
    public TeleportManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TeleportTask(plugin, waitingPlayers), 0, 20L);
    }

    public int getWaitingPlayers()
    {
        return waitingPlayers.size();
    }

    /**
     * Add player to teleport waiting queue
     *
     * @param player      The player.
     * @param destination The destionation.
     * @param msg         The message.
     */
    public void addPlayer(Player player, Location destination, String msg)
    {
        int secs = plugin.getSettingsManager().getTimeUntilTeleport();

        waitingPlayers.add(new TeleportState(plugin, player, destination, msg, secs));

        if (secs > 0) {
            player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("waiting.for.teleport.stand.still.for.0.seconds"), secs));
        }
    }


    public static boolean isLocationEqual(Location location1, Location location2, double fuzzy)
    {
        if (Math.abs(location1.getX() - location2.getX()) > fuzzy) {
            return false;
        }

        if (Math.abs(location1.getY() - location2.getY()) > fuzzy) {
            return false;
        }

        if (Math.abs(location1.getZ() - location2.getZ()) > fuzzy) {
            return false;
        }

        return true;
    }
}
