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


package com.p000ison.dev.simpleclans2.teleportation;

import com.p000ison.dev.simpleclans2.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a TeleportState
 */
public class TeleportState {

    private String playerName;
    private Location playerLocation;
    private Location destination;
    private int counter;
    private String msg;
    private boolean processing;

    public TeleportState(SimpleClans plugin, Player player, Location destination, String msg, int waiting)
    {
        this.destination = destination;
        this.playerLocation = player.getLocation();
        this.playerName = player.getName();
        this.msg = msg;
        this.counter = waiting;
    }

    /**
     * Gets the location the player was.
     *
     * @return The location.
     */
    public Location getLocation()
    {
        return playerLocation;
    }

    /**
     * Whether its time for teleport
     *
     * @return Whether its time for teleport.
     */
    public boolean isTeleportTime()
    {
        if (counter > 1) {
            counter--;
            return false;
        }

        return true;
    }

    /**
     * The player that is waiting for teleport.
     *
     * @return The player that is waiting for teleport
     */
    public Player getPlayer()
    {
        return Bukkit.getPlayer(playerName);
    }

    /**
     * Get seconds left before teleport
     *
     * @return Get seconds left before teleport.
     */
    public int getCounter()
    {
        return counter;
    }

    /**
     * Gets the message the player will get when he is teleporting.
     *
     * @return The message the player will get.
     */
    public String getMessage()
    {
        return msg;
    }

    /**
     * The location the player will teleport to.
     *
     * @return The location.
     */
    public Location getDestination()
    {
        return destination;
    }

    public boolean isProcessing()
    {
        return processing;
    }

    public void setProcessing(boolean processing)
    {
        this.processing = processing;
    }
}
