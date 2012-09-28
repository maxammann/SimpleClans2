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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private static final Set<String> availablePermissions = new HashSet<String>();

    static {
        availablePermissions.add("leader.demote");
        availablePermissions.add("manage.bb");
        availablePermissions.add("manage.ally");
        availablePermissions.add("manage.clanff");
        availablePermissions.add("manage.ranks");
    }

    public static void addAvailablePermission(String permission)
    {
        availablePermissions.add(permission);
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

    public String getName()
    {
        return name;
    }

    public String addPermission(String permission)
    {
        String lowerPerm = permission.toLowerCase();
        for (String perm : availablePermissions) {
            String loweriPerm = perm.toLowerCase();
            if (loweriPerm.equals(lowerPerm) || loweriPerm.startsWith(lowerPerm)) {
                permissions.add(perm);
                return perm;
            }
        }
        return null;
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

    public static void main(String[] args)
    {
//        String text = "";
//
//        for (int i = 0; i < 500; i++) {
//            for (int y = 0; y < 9; y++) {
//                text += y;
//                if (y % 9 == 0) {
//                    text += "|";
//                }
//            }
//        }
//
//        List<String> pages = split(text);
//        pages.toArray(new String[pages.size()]);
//
//        for (String s : split(text)) {
//            System.out.println(s);
//            System.out.println();
//        }
    }

    public static List<String> split(String text)
    {
        int MAX_CHARS = 256;
        int MAX_LINES = 13;
        int MAX_CHARS_PER_LINE = 18;

        List<String> finalPages = new ArrayList<String>();
        StringBuilder currentPage = new StringBuilder(text);

        int breaks = 0;
        int current = 0;
        int currentLineNumber = 0;

        while (true) {
            current++;
            if (current % MAX_CHARS_PER_LINE == 0) {
                currentLineNumber++;
                if (currentPage.length() > current) {
                    currentPage.insert(current, "\n");
                }
                breaks++;
            }

            if (currentLineNumber >= MAX_LINES || currentLineNumber > MAX_CHARS) {
                String newPage;
                if (currentPage.length() > current + breaks) {
                    newPage = currentPage.substring(0, current + breaks);
                    currentPage = new StringBuilder(currentPage.substring(current + breaks));
                } else {
                    newPage = currentPage.substring(0);
                    currentPage = null;
                }

                finalPages.add(newPage);
                //reset
                current = 0;
                breaks = 0;
                currentLineNumber = 0;
            }

            if (currentPage == null) {
                break;
            }
        }

        return finalPages;

    }
}