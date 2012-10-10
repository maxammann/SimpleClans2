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
 *     Created: 09.09.12 15:06
 */

package com.p000ison.dev.simpleclans2.clan.ranks;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Represents a Rank
 */
public class Rank implements Comparable<Rank> {
    private long id;

    private String name;
    private String tag;
    private Set<Integer> permissions = new HashSet<Integer>();
    private int priority;
    private boolean update;

    private static final Map<java.lang.Integer, String> availablePermissions = new TreeMap<java.lang.Integer, String>();

    static {
        availablePermissions.put(0, "leader.demote");
        availablePermissions.put(1, "manage.bb");
        availablePermissions.put(2, "manage.ally");
        availablePermissions.put(3, "manage.clanff");
        availablePermissions.put(4, "manage.ranks");
    }

    /**
     * Adds a available permission.
     *
     * @param permission The permission.
     * @param id         The assigned id.
     */
    public static void addAvailablePermission(String permission, int id)
    {
        availablePermissions.put(id, permission);
    }

    /**
     * Gets all available permission
     *
     * @return The available permissions.
     */
    public static Map<Integer, String> getAvailablePermissions()
    {
        return availablePermissions;
    }

    /**
     * Creates a new rank
     *
     * @param id   The id of the rank.
     * @param name The name of the rank.
     * @param tag  The tag
     */
    public Rank(long id, String name, String tag)
    {
        this.tag = tag;
        this.setId(id);
        this.name = name;
    }

    /**
     * Creates a new rank
     *
     * @param name     The name of the rank.
     * @param tag      The tag
     * @param priority The priority
     */
    public Rank(String name, String tag, int priority)
    {
        this.name = name;
        this.priority = priority;
        this.tag = tag;
    }

    /**
     * Creates a new rank
     *
     * @param id          The id of the rank.
     * @param name        The name of the rank.
     * @param tag         The tag
     * @param priority    The priority
     * @param permissions The permissions of this rank
     */
    public Rank(long id, String name, String tag, int priority, Set<Integer> permissions)
    {
        this(name, tag, priority);
        if (permissions == null) {
            this.permissions = new HashSet<Integer>();
        } else {
            this.permissions = permissions;
        }
        this.id = id;
    }


    /**
     * Checks if this permission has the permission.
     *
     * @param id The id of the permission.
     * @return Weather it has this permission.
     */
    public boolean hasPermission(int id)
    {
        return permissions.contains(id);
    }

    /**
     * Checks if this permission has the permission.
     *
     * @param permission The permission.
     * @return Weather it has this permission.
     */
    public boolean hasPermission(String permission)
    {
        return availablePermissions.values().contains(permission);
    }

    /**
     * Gets the name of this rank.
     *
     * @return The rank.
     */
    public String getName()
    {
        return name;
    }


    /**
     * Adds a permission to this rank. This implements a low searching method.
     *
     * @param permission The permission
     * @return The added permission.
     */
    public String addPermission(String permission)
    {
        String nearest = null;
        int id = -1;
        int smallest = Integer.MAX_VALUE;

        for (Map.Entry<Integer, String> perm : availablePermissions.entrySet()) {
            String key = perm.getValue();

            int distance = StringUtils.getLevenshteinDistance(key, permission);
            if (distance < smallest) {
                smallest = distance;
                id = perm.getKey();
                nearest = key;
            }
        }

        if (nearest != null && id != -1) {
            permissions.add(id);
            return nearest;
        }

        return null;
    }

    /**
     * Checks if a rank is more powerful than another one.
     *
     * @param rank The other rank.
     * @return Weather this is more powerful.
     */
    public boolean isMorePowerful(Rank rank)
    {
        return priority > rank.getPriority();
    }

    /**
     * Gets a set of permissions.
     *
     * @return A set of permissions.
     */
    public Set<Integer> getPermissions()
    {
        return permissions;
    }

    /**
     * Gets the priority of this rank.
     *
     * @return The priority
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of this clan
     *
     * @param priority The priority to set
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    /**
     * Gets the id of this clan
     *
     * @return The ID
     */
    public long getId()
    {
        return id;
    }

    /**
     * Sets the id of this clan
     *
     * @param id The id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Checks if 2 ranks equal.
     *
     * @param o The other rank
     * @return Weather they match
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || !(o instanceof Rank)) return false;

        Rank rank = (Rank) o;

        return id == rank.id;
    }

    /**
     * Gets a hashCode of this rank.
     *
     * @return The hash code of this rank
     */
    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }

    /**
     * Returns info about this rank.
     *
     * @return The rank in a rank.
     */
    @Override
    public String toString()
    {
        return "Rank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                ", priority=" + priority +
                '}';
    }

    /**
     * Checks if this rank should be pushed  to the database.
     *
     * @return Weather this  needs a update.
     */
    public boolean needsUpdate()
    {
        return update;
    }

    /**
     * Sets weather this needs a update
     *
     * @param update True of false
     */
    public void update(boolean update)
    {
        this.update = update;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    @Override
    public int compareTo(Rank anotherRank)
    {
        int thisPriority = this.getPriority();
        int anotherPriority = anotherRank.getPriority();
        return (thisPriority < anotherPriority ? -1 : (thisPriority == anotherPriority ? 0 : 1));
    }
}