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

import java.util.Set;

/**
 * Represents a Rank
 */
public class Rank {
    private long id;

    private String name;
    private long clanId;
    private Set<String> permissions;
    private int priority;

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

    public String getName()
    {
        return name;
    }

    public void addPermission(String permission)
    {
        permissions.add(permission);
    }

    public boolean hasPermission(String permission)
    {
        return permission.contains(permission);
    }

    public boolean isMorePowerful(Rank rank)
    {
        return priority > rank.getPriority();
    }

    public Set<String> getPermissions()
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

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
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
}
