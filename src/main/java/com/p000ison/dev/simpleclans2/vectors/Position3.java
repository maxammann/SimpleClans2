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
 *     Last modified: 10.11.12 16:45
 */

package com.p000ison.dev.simpleclans2.vectors;

import com.p000ison.dev.simpleclans2.Flagable;

/**
 * Represents a Position2
 */
public class Position3 implements Flagable {
    protected double x, y, z;

    public Position3()
    {
    }

    public Position3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position3(Position3 other)
    {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Position3 copy()
    {
        return new Position3(x, y, z);
    }

    public Position3 set(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Position3 set(Position3 other)
    {
        return set(other.x, other.y, other.z);
    }

    public Position3 add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Position3 add(Position3 other)
    {
        return add(other.x, other.y, other.z);
    }

    public Position3 subtract(double x, double y, double z)
    {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Position3 subtract(Position3 other)
    {
        return subtract(other.x, other.y, other.z);
    }

    public double distance(Position3 other)
    {
        return Math.sqrt(distanceSquared(other));
    }

    public double distance(double x, double y, double z)
    {
        return Math.sqrt(distanceSquared(x, y, z));
    }

    public double distanceSquared(Position3 other)
    {
        return distanceSquared(other.x, other.y, other.z);
    }

    public double distanceSquared(double x, double y, double z)
    {
        double distX = this.x - x;
        double distY = this.y - y;
        double distZ = this.z - z;
        return distX * distX + distY * distY + distZ * distZ;
    }

    @Override
    public String serialize()
    {
        return getX() + ":" + getY() + ":" + getZ();
    }

    @Override
    public Position3 deserialize(String deserialize)
    {
        String[] coords = deserialize.split(":");

        this.x = Double.parseDouble(coords[0]);
        this.y = Double.parseDouble(coords[1]);
        this.z = Double.parseDouble(coords[2]);

        return this;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}
