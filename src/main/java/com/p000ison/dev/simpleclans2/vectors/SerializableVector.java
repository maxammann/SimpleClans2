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
import org.bukkit.World;

/**
 * Represents a Vector
 */
public class SerializableVector {
    protected double x, y, z;
    protected World world;

    public SerializableVector(World world, double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public SerializableVector()
    {
    }

    public static SerializableVector deserialize(String string)
    {
        String[] coords = string.split(":");

        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        World world = Bukkit.getWorld(coords[3]);

        return new SerializableVector(world, x, y, z);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public World getWorld()
    {
        return world;
    }

    public String serialize()
    {
        return x + ":" + y + ":" + z + ":" + world.getName();
    }

    @Override
    public String toString()
    {
        return x + ":" + y + ":" + z + ":" + world.getName();
    }
}
