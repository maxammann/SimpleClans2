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
 *     Last modified: 10.11.12 16:50
 */

package com.p000ison.dev.simpleclans2.vectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a PlayerPosition
 */
public class PlayerPosition extends Position3 {
    private float pitch;
    private float yaw;
    private World world;

    public PlayerPosition()
    {

    }

    public PlayerPosition(World world, double x, double y, double z, float pitch, float yaw)
    {
        super(x, y, z);
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public PlayerPosition(Location location)
    {
        super(location.getX(), location.getY(), location.getZ());
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld();
    }

    public PlayerPosition(Position3 other, World world, float pitch, float yaw)
    {
        super(other);
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public PlayerPosition(PlayerPosition pos)
    {
        super(pos);
        this.pitch = pos.pitch;
        this.yaw = pos.yaw;
        this.world = pos.world;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    @Override
    public String serialize()
    {
        return getX() + ":" + getY() + ":" + getZ() + ":" + world.getName() + ":" + yaw + ":" + pitch;
    }

    @Override
    public PlayerPosition deserialize(String deserialize)
    {
        String[] coords = deserialize.split(":");

        this.x = Double.parseDouble(coords[0]);
        this.y = Double.parseDouble(coords[1]);
        this.z = Double.parseDouble(coords[2]);
        this.world = Bukkit.getWorld(coords[3]);
        this.yaw = Float.parseFloat(coords[4]);
        this.pitch = Float.parseFloat(coords[5]);

        return this;
    }

    public Location toLocation()
    {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
