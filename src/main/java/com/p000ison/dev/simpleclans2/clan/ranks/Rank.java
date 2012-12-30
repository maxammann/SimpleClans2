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
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.clan.ranks;

import com.p000ison.dev.simpleclans2.util.JSONUtil;
import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a Rank
 */
@DatabaseTable(name = "sc2_ranks")
public class Rank implements Comparable<Rank>, TableObject {
    private static final Map<java.lang.Integer, String> availablePermissions = new TreeMap<java.lang.Integer, String>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            return o1.compareTo(o2);
        }
    });

    static {
        availablePermissions.put(0, "leader.demote");
        availablePermissions.put(1, "manage.bb");
        availablePermissions.put(2, "manage.ally");
        availablePermissions.put(3, "manage.clanff");
        availablePermissions.put(4, "manage.ranks");
        availablePermissions.put(5, "manage.trusted");
        availablePermissions.put(7, "manage.invites");

        //unimplemented
        availablePermissions.put(5, "bank.deposit");
        availablePermissions.put(6, "bank.withdraw");
    }

    @DatabaseColumn(position = 0, databaseName = "id", id = true)
    private int id;
    @DatabaseColumn(position = 2, databaseName = "name", lenght = 16)
    private String name;
    @DatabaseColumn(position = 1, databaseName = "tag", lenght = 16)
    private String tag;
    private Map<Integer, Boolean> permissions;
    @DatabaseColumn(position = 4, databaseName = "priority", lenght = 3)
    private int priority;
    @DatabaseColumn(position = 3, databaseName = "clan")
    private int clanId;
    private boolean update;

    public Rank()
    {
    }

    /**
     * Creates a new rank
     *
     * @param id   The id of the rank.
     * @param name The name of the rank.
     * @param tag  The tag
     */
    public Rank(int id, String name, String tag)
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
    public Rank(String name, String tag, int priority, int clanId)
    {
        this.name = name;
        this.priority = priority;
        this.tag = tag;
        this.clanId = clanId;
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
    public Rank(int id, String name, String tag, int priority, Map<Integer, Boolean> permissions, int clanId)
    {
        this(name, tag, priority, clanId);
        if (permissions == null) {
            this.permissions = new HashMap<Integer, Boolean>();
        } else {
            this.permissions = permissions;
        }

        this.id = id;
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

    public static int getID(String permission)
    {
        for (Map.Entry<Integer, String> entry : availablePermissions.entrySet()) {
            if (entry.getValue().equals(permission)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static String getByID(int id)
    {
        for (Map.Entry<Integer, String> entry : availablePermissions.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String removePermission(String permission)
    {
        if (permissions.isEmpty()) {
            return null;
        }

        PermissionFinder permissionFinder = new PermissionFinder(permission);
        if (permissionFinder.getPermission() != null && id != -1) {
            int id = permissionFinder.getId();
            if (permissions.remove(id)) {
                return permissionFinder.getPermission();
            }
        }

        return null;
    }

    /**
     * Checks if this permission has the permission.
     *
     * @param id The id of the permission.
     * @return Weather it has this permission.
     */
    public boolean hasPermission(int id)
    {
        Boolean perm = permissions.get(id);
        return perm == null || !perm;
    }

    /**
     * Checks if this permission has the permission.
     *
     * @param permission The permission.
     * @return Weather it has this permission.
     */
    public boolean hasPermission(String permission)
    {
        return hasPermission(getID(permission));
    }

    public boolean isNegative(int id)
    {
        Boolean permission = permissions.get(id);

        if (permission == null) {
            return false;
        }

        return !permission;
    }

    public boolean isNegative(String permission)
    {
        return isNegative(getID(permission));
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
    public String addPermission(String permission, boolean positive)
    {
        PermissionFinder permissionFinder = new PermissionFinder(permission);

        if (permissionFinder.getPermission() != null && id != -1) {
            permissions.put(permissionFinder.getId(), positive);
            return permissionFinder.getPermission();
        }

        return null;
    }

    public int getClanId()
    {
        return clanId;
    }

    private class PermissionFinder {
        private String permission;
        private int id;

        private PermissionFinder(String permission)
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

            this.id = id;
            this.permission = nearest;
        }

        public String getPermission()
        {
            return permission;
        }


        public int getId()
        {
            return id;
        }
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
    public Map<Integer, Boolean> getPermissions()
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
    public int getId()
    {
        return id;
    }

    /**
     * Sets the id of this clan
     *
     * @param id The id to set
     */
    public void setId(int id)
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
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Rank)) {
            return false;
        }

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

    private String getDatabasePermissions()
    {
        return JSONObject.toJSONString(permissions);
    }

    private void setDatabasePermissions(String json)
    {
        if (json == null) {
            permissions = new HashMap<Integer, Boolean>();
        }
        this.permissions = JSONUtil.JSONToPermissionMap(json);
    }
}