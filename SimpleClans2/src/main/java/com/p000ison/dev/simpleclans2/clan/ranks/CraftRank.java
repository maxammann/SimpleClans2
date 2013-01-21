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

import com.p000ison.dev.simpleclans2.api.UpdateAble;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.util.JSONUtil;
import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnGetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnSetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a CraftRank
 */
@DatabaseTable(name = "sc2_ranks")
public class CraftRank implements Rank, TableObject {
    private static final Map<java.lang.Integer, String> availablePermissions = new TreeMap<java.lang.Integer, String>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
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
    private AtomicLong id = new AtomicLong();
    @DatabaseColumn(position = 2, databaseName = "name", lenght = 16)
    private String name;
    @DatabaseColumn(position = 1, databaseName = "tag", lenght = 16)
    private String tag;
    private Map<Integer, Boolean> permissions;
    @DatabaseColumn(position = 4, databaseName = "priority", lenght = 3)
    private AtomicInteger priority = new AtomicInteger(0);
    @DatabaseColumn(position = 3, databaseName = "clan")
    private AtomicLong clanId = new AtomicLong(-1L);
    private AtomicBoolean update = new AtomicBoolean(false);

    private final static Object nameLock = new Object();

    public CraftRank() {
    }

    /**
     * Creates a new rank
     *
     * @param name     The name of the rank.
     * @param tag      The tag
     * @param priority The priority
     */
    public CraftRank(String name, String tag, int priority, long clanId) {
        setName(name);
        setPriority(priority);
        setTag(tag);
        setID(clanId);
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
    public CraftRank(int id, String name, String tag, int priority, Map<Integer, Boolean> permissions, int clanId) {
        this(name, tag, priority, clanId);
        if (permissions == null) {
            this.permissions = new ConcurrentHashMap<Integer, Boolean>();
        } else {
            this.permissions = permissions;
        }

        setID(id);
    }

    /**
     * Adds a available permission.
     *
     * @param permission The permission.
     * @param id         The assigned id.
     */
    public static void addAvailablePermission(String permission, int id) {
        availablePermissions.put(id, permission);
    }

    /**
     * Gets all available permission
     *
     * @return The available permissions.
     */
    public static Map<Integer, String> getAvailablePermissions() {
        return availablePermissions;
    }

    public static int getID(String permission) {
        for (Map.Entry<Integer, String> entry : availablePermissions.entrySet()) {
            if (entry.getValue().equals(permission)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static String getByID(int id) {
        for (Map.Entry<Integer, String> entry : availablePermissions.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void setName(String name) {
        synchronized (nameLock) {
            this.name = name;
        }
    }

    @Override
    public String removePermission(String permission) {
        if (permissions == null || permissions.isEmpty()) {
            return null;
        }

        PermissionFinder permissionFinder = new PermissionFinder(permission);
        if (permissionFinder.getPermission() != null && id.get() != -1) {
            int id = permissionFinder.getId();
            if (permissions.remove(id)) {
                return permissionFinder.getPermission();
            }
        }

        return null;
    }

    @Override
    public boolean hasPermission(int id) {
        if (permissions == null) {
            return false;
        }
        Boolean perm = permissions.get(id);
        return perm == null || !perm;
    }

    @Override
    public boolean hasPermission(String permission) {
        return hasPermission(getID(permission));
    }

    @Override
    public boolean isNegative(int id) {
        if (permissions == null) {
            return false;
        }

        Boolean permission = permissions.get(id);

        return permission != null && !permission;

    }

    @Override
    public boolean isNegative(String permission) {
        return isNegative(getID(permission));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String addPermission(String permission, boolean positive) {
        if (permissions == null) {
            permissions = new ConcurrentHashMap<Integer, Boolean>();
        }

        PermissionFinder permissionFinder = new PermissionFinder(permission);

        if (permissionFinder.getPermission() != null && id.get() != -1) {
            permissions.put(permissionFinder.getId(), positive);
            return permissionFinder.getPermission();
        }

        return null;
    }

    @Override
    public long getClanID() {
        return clanId.get();
    }

    private static class PermissionFinder {
        private String permission;
        private int id;

        private PermissionFinder(String permission) {
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

        public String getPermission() {
            return permission;
        }


        public int getId() {
            return id;
        }
    }

    @Override
    public boolean isMorePowerful(Rank rank) {
        return priority.get() > rank.getPriority();
    }

    @Override
    public Map<Integer, Boolean> getPermissions() {
        return permissions;
    }

    @Override
    public int getPriority() {
        return priority.get();
    }

    @Override
    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    @Override
    public long getID() {
        return id.get();
    }

    @Override
    public void setID(long id) {
        this.id.set(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof CraftRank)) {
            return false;
        }

        CraftRank rank = (CraftRank) o;

        return id.get() == rank.id.get();
    }

    @Override
    public int hashCode() {
        return (int) (id.get() ^ (id.get() >>> 32));
    }

    @Override
    public String toString() {
        return "CraftRank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean needsUpdate() {
        return update.get();
    }

    @Override
    public void update() {
        update(true);
    }

    @Override
    public void update(boolean update) {
        this.update.set(update);
    }

    @Override
    public long getLastUpdated() {
        return UpdateAble.NEVER_UPDATED;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        synchronized (nameLock) {
            this.tag = tag;
        }
    }

    @Override
    public int compareTo(Rank anotherRank) {
        int thisPriority = this.getPriority();
        int anotherPriority = anotherRank.getPriority();
        return (thisPriority < anotherPriority ? -1 : (thisPriority == anotherPriority ? 0 : 1));
    }

    @DatabaseColumnGetter(databaseName = "permissions")
    private String getDatabasePermissions() {
        if (permissions == null) {
            return null;
        }
        return JSONObject.toJSONString(permissions);
    }

    @DatabaseColumnSetter(position = 4, databaseName = "permissions")
    private void setDatabasePermissions(String json) {
        if (json == null) {
            permissions = null;
            return;
        }
        this.permissions = JSONUtil.JSONToPermissionMap(json, new ConcurrentHashMap<Integer, Boolean>());
    }
}