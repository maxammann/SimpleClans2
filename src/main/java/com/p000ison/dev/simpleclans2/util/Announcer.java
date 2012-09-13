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
 *     Created: 11.09.12 20:13
 */

package com.p000ison.dev.simpleclans2.util;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.Bukkit;

/**
 * Represents a Announcer
 */
public class Announcer {

    private static SimpleClans plugin;

    public static void announce(ClanPlayer clanPlayer, String message)
    {
        announceRaw(GeneralHelper.parseColors(plugin.getSettingsManager().getClanPlayerAnnounce().replace("+player", clanPlayer.getName()).replace("+message", message)));
    }

    public static void announce(Clan clan, String message)
    {
        announceRaw(GeneralHelper.parseColors(plugin.getSettingsManager().getClanAnnounce().replace("+clan", clan.getTag()).replace("+message", message)));
    }

    public static void announce(String message)
    {
        announceRaw(GeneralHelper.parseColors(plugin.getSettingsManager().getDefaultAnnounce().replace("+message", message)));
    }

    private static void announceRaw(String message)
    {
        if (message == null) {
            return;
        }

        Bukkit.broadcastMessage(message);
    }

    public static void setPlugin(SimpleClans plugin)
    {
        Announcer.plugin = plugin;
    }

    public static SimpleClans getPlugin()
    {
        return plugin;
    }

    public static void clear()
    {
        setPlugin(null);
    }
}
