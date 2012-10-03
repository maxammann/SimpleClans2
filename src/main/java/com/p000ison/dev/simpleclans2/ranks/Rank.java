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

package com.p000ison.dev.simpleclans2.ranks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Rank
 */
public class Rank {
    private long id;

    private String name;
    private long clanId;
    private Set<Integer> permissions = new HashSet<Integer>();
    private int priority;
    private boolean update;

    private static final Map<String, Integer> availablePermissions = new HashMap<String, Integer>();

    static {
        availablePermissions.put("leader.demote", 0);
        availablePermissions.put("manage.bb", 1);
        availablePermissions.put("manage.ally", 2);
        availablePermissions.put("manage.clanff", 3);
        availablePermissions.put("manage.ranks", 4);
    }

    public static void addAvailablePermission(String permission, int id)
    {
        availablePermissions.put(permission, id);
    }

    public Rank(long id, String name)
    {
        this.setId(id);
        this.name = name;
    }

    public Rank(String name, int priority)
    {
        this.name = name;
        this.priority = priority;
    }

    public Rank(long id, String name, int priority, long clanId, Set<Integer> permissions)
    {
        this(name, priority);
        this.clanId = clanId;
        if (permissions == null) {
            this.permissions = new HashSet<Integer>();
        } else {
            this.permissions = permissions;
        }
        this.id = id;
    }


    public boolean hasPermission(int id)
    {
        return permissions.contains(id);
    }

    public boolean hasPermission(String permission)
    {
        return permissions.contains(availablePermissions.get(permission));
    }

    public String getName()
    {
        return name;
    }

    public String addPermission(String permission)
    {
        String lowerPerm = permission.toLowerCase();
        for (Map.Entry<String, Integer> perm : availablePermissions.entrySet()) {
            String key = perm.getKey();
            String cleanKey = key.toLowerCase();

            if (cleanKey.startsWith(lowerPerm)) {
                permissions.add(perm.getValue());
                return key;
            }
        }
        return null;
    }

    public boolean isMorePowerful(Rank rank)
    {
        return priority > rank.getPriority();
    }

    public Set<Integer> getPermissions()
    {
        return permissions;
    }

    public int getPriority()
    {
        return priority;
    }

    public long getClanId()
    {
        return clanId;
    }

    public void setClanId(long clanId)
    {
        this.clanId = clanId;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || !(o instanceof Rank)) return false;

        Rank rank = (Rank) o;

        return id == rank.id;
    }

    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString()
    {
        return "Rank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clanId=" + clanId +
                ", permissions=" + permissions +
                ", priority=" + priority +
                '}';
    }

    public boolean needsUpdate()
    {
        return update;
    }

    public void update(boolean update)
    {
        this.update = update;
    }
}