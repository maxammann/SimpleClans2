/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
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

    private SimpleClans plugin;
    private String playerName;
    private Location playerLocation;
    private Location destination;
    private int counter;
    private String msg;
    private boolean processing;

    public TeleportState(SimpleClans plugin, Player player, Location dest, String msg, int waiting)
    {
        this.plugin = plugin;
        this.destination = dest;
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
