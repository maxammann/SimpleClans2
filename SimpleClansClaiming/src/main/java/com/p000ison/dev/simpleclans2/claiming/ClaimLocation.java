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
 *     Last modified: 27.02.13 14:04
 */

package com.p000ison.dev.simpleclans2.claiming;

import org.bukkit.Location;

/**
 * Represents a ClaimLocation
 */
public class ClaimLocation {
    private byte worldID;
    private int x, z;
    private short y;

    public ClaimLocation() {
    }

    public ClaimLocation(int worldID, int x, int z) {
        this.worldID = (byte) worldID;
        this.x = x;
        this.z = z;
        this.y = 0;
    }

    public ClaimLocation(int worldID, int x, int z, short y) {
        this.worldID = (byte) worldID;
        this.x = x;
        this.z = z;
        this.y = y;
    }

    public ClaimLocation(int worldID, int x, int z, int y) {
        this.worldID = (byte) worldID;
        this.x = x;
        this.z = z;
        this.y = (short) y;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setY(int y) {
        this.y = (short) y;
    }

    public double distance(ClaimLocation location) {
        return Math.sqrt(Math.pow(x - location.x, 2) * Math.pow(z - location.z, 2));
    }

    public int distanceRounded(ClaimLocation location) {
        return (int) Math.round(distance(location));
    }

    public static ClaimLocation toClaimLocation(Location location, int world, int size, int height) {
        return new ClaimLocation(world, location.getBlockX() / size, location.getBlockZ() / size, (short) (location.getBlockY() / height));
    }

    public static ClaimLocation toClaimLocation(Location location, int world) {
        return toClaimLocation(location, world, 16, 256);
    }

    @Override
    public String toString() {
        return "ClaimLocation{" +
                "worldID=" + worldID +
                ", x=" + x +
                ", z=" + z +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClaimLocation that = (ClaimLocation) o;

        if (worldID != that.worldID) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) worldID;
        result = 31 * result + x;
        result = 31 * result + z;
        result = 31 * result + (int) y;
        return result;
    }

    public static ClaimLocation toLocation(String data) {
        return null;
    }

    public byte getWorld() {
        return worldID;
    }


}
