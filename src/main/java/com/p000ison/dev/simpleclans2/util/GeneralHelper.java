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
 *     Created: 02.09.12 23:44
 */

package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a GeneralHelper
 */
public class GeneralHelper {

    private GeneralHelper()
    {
    }

    public static String arrayToString(String... args)
    {
        if (args == null || args.length == 0) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        for (Object string : args) {
            out.append(string).append(' ');

        }

        return out.substring(0, out.length() - 1);
    }

    public static String arrayToString(char... args)
    {
        StringBuilder string = new StringBuilder();

        for (char color : args) {
            string.append(ChatColor.getByChar(color).name().toLowerCase()).append(' ');
        }

        return string.substring(0, string.length() - 1);
    }

    public static Set<ClanPlayer> stripOfflinePlayers(Set<ClanPlayer> players)
    {
        Set<ClanPlayer> out = new HashSet<ClanPlayer>();
        for (ClanPlayer clanPlayer : players) {

            if (clanPlayer.toPlayer() != null) {
                out.add(clanPlayer);
            }

        }

        return out;
    }

    public static String clansToString(Collection<Clan> collection, String separator)
    {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        StringBuilder string = new StringBuilder();

        for (Clan clan : collection) {
            string.append(clan.getTag()).append(separator);

        }

        return string.substring(0, string.length() - separator.length());
    }


    public static String arrayBoundsToString(int start, int end, String... args)
    {
        return arrayToString(Arrays.copyOfRange(args, start, end));
    }

    public static String arrayBoundsToString(int start, String... args)
    {
        return arrayToString(Arrays.copyOfRange(args, start, args.length));
    }

    public static boolean containsColor(String test, char alternateChar, Character color)
    {
        return test.contains(String.valueOf(alternateChar) + String.valueOf(color));
    }

    public static boolean containsColor(String test, char alternateChar, char... colors)
    {
        for (char color : colors) {
            if (containsColor(test, alternateChar, color)) {
                return true;
            }
        }

        return false;
    }

    public static boolean containsColor(String test, char alternateChar, ChatColor... colors)
    {
        for (ChatColor color : colors) {
            if (!containsColor(test, alternateChar, color.getChar())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convert color hex values
     *
     * @param msg The message to colorize
     * @return The colored string
     */
    public static String parseColors(String msg)
    {
        return msg.replace("&", "\u00a7");
    }

    public static String locationToString(Location loc)
    {
        return loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " " + loc.getWorld().getName();
    }
}
