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
 * Represents a ChunkLocation
 */
public class ChunkLocation {
    private final int x, z;

    public ChunkLocation(int x, int y) {
        this.x = x;
        this.z = y;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    public double distance(ChunkLocation location) {
        return Math.sqrt(Math.pow(x - location.x, 2) * Math.pow(z - location.z,2));
    }

    public int distanceRounded(ChunkLocation location) {
        return (int) Math.round(distance(location));
    }

    public static ChunkLocation toChunkLocation(Location player) {
        return new ChunkLocation(player.getBlockX() >> 4, player.getBlockZ() >> 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ChunkLocation)) {
            return false;
        }

        ChunkLocation that = (ChunkLocation) o;

        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
