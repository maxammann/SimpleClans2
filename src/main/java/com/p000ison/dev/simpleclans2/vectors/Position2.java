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
 *     Last modified: 10.11.12 16:34
 */

package com.p000ison.dev.simpleclans2.vectors;

/**
 * Represents a Position2
 */
public class Position2  {
    protected double x, y;

    public Position2()
    {
    }

    public Position2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Position2(Position2 other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public Position2 copy()
    {
        return new Position2(x, y);
    }

    public Position2 set(double x, double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Position2 set(Position2 other)
    {
        return set(other.x, other.y);
    }

    public Position2 add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Position2 add(Position2 other)
    {
        return add(other.x, other.y);
    }

    public Position2 subtract(double x, double y)
    {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Position2 subtract(Position2 other)
    {
        return subtract(other.x, other.y);
    }

    public double distance(Position2 other)
    {
        return Math.sqrt(distanceSquared(other));
    }

    public double distance(double x, double y)
    {
        return Math.sqrt(distanceSquared(x, y));
    }

    public double distanceSquared(Position2 other)
    {
        return distanceSquared(other.x, other.y);
    }

    public double distanceSquared(double x, double y)
    {
        double distX = this.x - x;
        double distY = this.y - y;
        return distX * distX + distY * distY;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public String serialize()
    {
        return getX() + ":" + getY();
    }

    public static Position2 deserialize(String deserialize)
    {
        String[] coords = deserialize.split(":");

        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);

        return new Position2(x, y);
    }
}
