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
