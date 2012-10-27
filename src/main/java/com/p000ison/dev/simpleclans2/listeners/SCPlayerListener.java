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


package com.p000ison.dev.simpleclans2.listeners;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.Command;
import com.p000ison.dev.simpleclans2.database.data.response.responses.BBRetrieveResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

/**
 * Internally used
 */
@SuppressWarnings("unused")
public class SCPlayerListener implements Listener {

    private SimpleClans plugin;

    public SCPlayerListener(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDisconnect(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        plugin.getRequestManager().clearRequests(clanPlayer);

        if (clanPlayer != null) {
            clanPlayer.updateLastSeen();
            clanPlayer.removePermissions();
            clanPlayer.removeOnlineVersion();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer != null) {
            clanPlayer.setupOnlineVersion();
            clanPlayer.updatePermissions();
            Clan clan = clanPlayer.getClan();

            if (plugin.getSettingsManager().isMotdBBEnabled()) {
                plugin.getDataManager().addResponse(new BBRetrieveResponse(plugin, player, clan, -1, plugin.getSettingsManager().getMotdBBLines(), plugin.getSettingsManager().getMotdBBFormat()));
            }

            clan.updateLastAction();
            clan.update();
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String[] args = message.substring(1).split(" ");
        int lenght = args.length;

        String clanCommand = plugin.getSettingsManager().getClanCommand();
        String rankCommand = plugin.getSettingsManager().getClanCommand();
        String bbCommand = plugin.getSettingsManager().getClanCommand();

        Command.Type type = null;

        if (args[0].equalsIgnoreCase(clanCommand)) {
            type = Command.Type.CLAN;
        } else if (args[0].equalsIgnoreCase(rankCommand)) {
            type = Command.Type.RANK;
        } else if (args[0].equalsIgnoreCase(bbCommand)) {
            type = Command.Type.BB;
        }

        if (type != null) {
            plugin.getCommandManager().execute(player, args[0], type, Arrays.copyOfRange(args, 1, lenght));
            event.setCancelled(true);
        }
    }
}
