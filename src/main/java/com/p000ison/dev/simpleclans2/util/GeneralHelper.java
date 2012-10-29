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

package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a GeneralHelper
 */
public final class GeneralHelper {

    private GeneralHelper()
    {
    }

    public static String arrayToString(String... args)
    {
        return arrayToString(" ", args);
    }

    public static String arrayToString(String seperator, String... args)
    {
        if (args == null || args.length == 0) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        for (Object string : args) {
            out.append(string).append(seperator);

        }

        return out.substring(0, out.length() - seperator.length());
    }

    public static String arrayToString(String seperator, Collection collection)
    {
        if (collection == null || collection.size() == 0) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        for (Object string : collection) {
            out.append(string.toString()).append(seperator);

        }

        return out.substring(0, out.length() - seperator.length());
    }

    public static boolean isValidEmailAddress(String emailAddress)
    {
        if (emailAddress == null) {
            return false;
        }
        String expression = "^[\\w\\-]([\\.\\w\\-])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    public static boolean isOnline(Player player)
    {
        if (player == null) {
            return false;
        }

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.canSee(player)) {
                return false;
            }
        }

        return true;
    }

    public static String arrayToString(char... args)
    {
        return arrayToString(" ", args);
    }

    public static String arrayToString(String seperator, char... args)
    {
        StringBuilder string = new StringBuilder();

        for (char color : args) {
            string.append(ChatColor.getByChar(color).name().toLowerCase(Locale.US)).append(seperator);
        }

        return string.substring(0, string.length() - seperator.length());
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

    public static String clansPlayersToString(Collection<ClanPlayer> collection, String separator)
    {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        StringBuilder string = new StringBuilder();

        for (ClanPlayer cp : collection) {
            string.append(cp.getName()).append(separator);

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
        return test.contains(alternateChar + String.valueOf(color));
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

    public static String locationToString(Location loc)
    {
        return loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " " + loc.getWorld().getName();
    }

    public static boolean checkConnection(String url)
    {
        try {
            URL Url = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) Url.openConnection();
            urlConn.connect();

            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static boolean deleteWorld(World world)
    {
        return Bukkit.unloadWorld(world, false) && deleteDir(world.getWorldFolder());
    }

    public static boolean deleteDir(File path)
    {

        if (path == null || !path.isDirectory()) {
            return false;
        }

        for (File file : path.listFiles()) {

            if (file.isDirectory()) {
                deleteDir(file);
            }

            if (!file.delete()) {
                return false;
            }
        }

        return path.delete();
    }
}
