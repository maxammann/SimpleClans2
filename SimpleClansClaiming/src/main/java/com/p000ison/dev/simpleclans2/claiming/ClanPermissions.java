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

/*
MEMBERS  ALLIES   RIVALS   OUTSIDERS
00000000 00000000 00000000 00000000 | 00000000 00000000 00000000 00000000
                                                        > BUILD
                                                         > BREAK
                                                          > SWITCH
                                                           > ITEM_USE
                                                            > ?
                                                             > ?
                                                              > ?
                                                               > ?
 */
public final class ClanPermissions {

    private long permissions;

    /**
     * The amount of permissions, currently: 4
     */
    private static final int PERMISSIONS = Byte.SIZE;

    private static final int CLEAR_MASK;

    static {
        int perms = 0;

        for (Field field : ClanPermissions.class.getFields()) {
            if (field.getName().startsWith("PERM_")) {
                perms++;
            }
        }

        if (perms >= PERMISSIONS) {
            throw new IllegalArgumentException("There are too many permissions defined!");
        }

        int mask = 0;

        for (int i = 0; i < PERMISSIONS; i++) {
            mask |= i;
        }

        CLEAR_MASK = mask;
    }

    /**
     * The different permissions
     */
    public static final int PERM_BUILD = 1, PERM_BREAK = 1 << 1, PERM_SWITCH = 1 << 2, PERM_ITEM_USE = 1 << 3;

    /**
     * The different groups
     */
    public static final int MEMBERS = 0, ALLIES = 1, OUTSIDERS = 2, RIVALS = 3;

    public void setPermissions(int group, int permission, boolean value) {
        if (value) {
            this.permissions |= permission << group * PERMISSIONS;
        } else {
            togglePermissions(group, permission);
        }
    }

    public void togglePermissions(int group, int permission) {
        this.permissions ^= permission << group * PERMISSIONS;
    }

    public void clearPermissions(int group) {
        this.permissions &= ~(CLEAR_MASK << group * PERMISSIONS);
    }

    public boolean hasPermission(int group, int permission) {
        return ((permissions >> group * PERMISSIONS) & permission) == permission;
    }

    public long getValue() {
        return permissions;
    }
}
