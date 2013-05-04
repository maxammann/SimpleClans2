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
 *     Last modified: 09.01.13 19:15
 */

package com.p000ison.dev.simpleclans2.api.rank;

import com.p000ison.dev.simpleclans2.api.UpdateAble;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public interface Rank extends Comparable<Rank>, UpdateAble, Serializable {

    String removePermission(String permission);

    /**
     * Checks if this permission has the permission.
     *
     * @param id The id of the permission.
     * @return Weather it has this permission.
     */
    boolean hasPermission(int id);

    /**
     * Checks if this permission has the permission.
     *
     * @param permission The permission.
     * @return Weather it has this permission.
     */
    boolean hasPermission(String permission);

    boolean isNegative(int id);

    boolean isNegative(String permission);

    /**
     * Gets the name of this rank.
     *
     * @return The rank.
     */
    String getName();

    /**
     * Adds a permission to this rank. This implements a low searching method.
     *
     * @param permission The permission
     * @return The added permission.
     */
    String addPermission(String permission, boolean positive);

    long getClanID();

    /**
     * Checks if a rank is more powerful than another one.
     *
     * @param rank The other rank.
     * @return Weather this is more powerful.
     */
    boolean isMorePowerful(Rank rank);

    /**
     * Gets a set of permissions.
     *
     * @return A set of permissions.
     */
    Map<Integer, Boolean> getPermissions();

    /**
     * Gets the priority of this rank.
     *
     * @return The priority
     */
    int getPriority();

    /**
     * Sets the priority of this clan
     *
     * @param priority The priority to set
     */
    void setPriority(int priority);

    /**
     * Gets the id of this clan
     *
     * @return The ID
     */
    long getID();

    /**
     * Sets the id of this clan of this rank
     *
     * @param id The id to set
     */
    void setClanID(long id);

    /**
     * Checks if 2 ranks equal.
     *
     * @param o The other rank
     * @return Weather they match
     */
    @Override
    boolean equals(Object o);

    /**
     * Gets a hashCode of this rank.
     *
     * @return The hash code of this rank
     */
    @Override
    int hashCode();

    /**
     * Returns info about this rank.
     *
     * @return The rank in a rank.
     */
    @Override
    String toString();

    /**
     * Checks if this rank should be pushed  to the database.
     *
     * @return Weather this  needs a update.
     */
    boolean needsUpdate();

    /**
     * Sets weather this needs a update
     *
     * @param update True of false
     */
    @Override
    void update(boolean update);

    String getTag();

    void setTag(String tag);
}
