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
 *     Last modified: 01.03.13 19:15
 */

package com.p000ison.dev.simpleclans2.claiming;

import java.lang.reflect.Field;

/**
 * Represents a ClanPermissions
 */
public final class ClanPermissions {

    private long permissions = 0;

    /**
     * The amount of permissions, currently: {@link #PERM_BREAK}, {@link #PERM_BUILD}, {@link #PERM_INTERACT}
     */
    private static final int PERMISSIONS;

    static {
        int perms = 0;

        for (Field field : ClanPermissions.class.getFields()) {
            if (field.getName().startsWith("PERM_")) {
                perms++;
            }
        }

        PERMISSIONS = perms;
    }

    /**
     * The different permissions
     */
    public static final int PERM_BUILD = 1, PERM_BREAK = 1 << 1, PERM_INTERACT = 1 << 2;
    /**
     * The different groups
     */
    public static final int ALLIES = 0, RIVALS = 1, OUTSIDERS = 2;

    public void setPermissions(int group, int permission) {
        this.permissions |= permissions << group * PERMISSIONS;
    }

    public void clearPermissions(int group) {
        this.permissions &= ~(7 << group * PERMISSIONS);
    }

    public boolean hasPermission(int permission, int group) {
        return ((permissions >> group * PERMISSIONS) & permission) == permission;
    }
}
