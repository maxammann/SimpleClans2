/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.vectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a SerializablePlayerVector
 */
public class SerializablePlayerVector extends SerializableVector {
    private float pitch;
    private float yaw;

    public SerializablePlayerVector(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public SerializablePlayerVector(World world, double x, double y, double z, float pitch, float yaw)
    {
        super(world, x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SerializablePlayerVector(Location location)
    {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public static SerializablePlayerVector deserialize(String string)
    {
        String[] coords = string.split(":");

        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        World world = Bukkit.getWorld(coords[3]);
        float pitch = Float.parseFloat(coords[4]);
        float yaw = Float.parseFloat(coords[5]);

        return new SerializablePlayerVector(world, x, y, z, pitch, yaw);
    }

    public Location toLocation()
    {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public String serialize()
    {
        return super.getX() + ":" + super.getY() + ":" + super.getZ() + ":" + super.getWorld().getName();
    }
}
