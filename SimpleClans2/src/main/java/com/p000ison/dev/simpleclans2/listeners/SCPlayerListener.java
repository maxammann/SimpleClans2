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

import com.p000ison.dev.commandlib.CommandSender;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.commands.ClanPlayerSender;
import com.p000ison.dev.simpleclans2.clanplayer.CraftClanPlayer;
import com.p000ison.dev.simpleclans2.database.response.responses.BBRetrieveResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Internally used
 */
@SuppressWarnings("unused")
public class SCPlayerListener implements Listener {

    private SimpleClans plugin;

    public SCPlayerListener(SimpleClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getAnyClanPlayer(player);

        plugin.getRequestManager().clearRequests(player);

        if (clanPlayer != null) {
            clanPlayer.updateLastSeen();
            ((CraftClanPlayer) clanPlayer).removePermissions();
            ((CraftClanPlayer) clanPlayer).removeOnlineVersion();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer != null) {
            ((CraftClanPlayer) clanPlayer).setupOnlineVersion();
            ((CraftClanPlayer) clanPlayer).updatePermissions();
            Clan clan = clanPlayer.getClan();

            if (clan != null) {
                if (plugin.getSettingsManager().isMotdBBEnabled() && clanPlayer.isBBEnabled()) {
                    plugin.getDataManager().addResponse(new BBRetrieveResponse(plugin, player, clan, -1, plugin.getSettingsManager().getMotdBBLines(), false));
                }

                clan.updateLastAction();
                clan.update();
            }

            clanPlayer.updateLastSeen();
            clanPlayer.update();
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().substring(1);

		String[] args = message.substring(1).split(" ");

		String clanCommand = plugin.getSettingsManager().getClanCommand();
		String rankCommand = plugin.getSettingsManager().getRankCommand();
		String bbCommand = plugin.getSettingsManager().getBBCommand();
		String bankCommand = plugin.getSettingsManager().getBankCommand();

		if (!(args[0].equalsIgnoreCase(clanCommand)
				|| args[0].equalsIgnoreCase(rankCommand)
				|| args[0].equalsIgnoreCase(bbCommand)
				||args[0].equalsIgnoreCase(bbCommand))) {
			return;
		}


		ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);
        CommandSender cmdSender = new ClanPlayerSender(player, cp);

        plugin.getCommandManager().executeAll(cmdSender, message);
        event.setCancelled(true);
    }
}
