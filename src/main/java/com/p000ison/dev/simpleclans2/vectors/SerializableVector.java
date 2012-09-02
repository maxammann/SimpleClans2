/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
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
