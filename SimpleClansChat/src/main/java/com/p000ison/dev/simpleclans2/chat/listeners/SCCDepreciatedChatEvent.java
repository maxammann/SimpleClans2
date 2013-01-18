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
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat.listeners;

import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Represents a SCCDepreciatedChatEvent
 */
@SuppressWarnings("deprecation")
public class SCCDepreciatedChatEvent implements Listener {

    private SimpleClansChat plugin;

    public SCCDepreciatedChatEvent(SimpleClansChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerChatEvent event) {
        String out = null;
        Player player = event.getPlayer();
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (this.plugin.getSettingsManager().isCompatibilityMode()) {
            out = plugin.formatCompatibility(event.getFormat(), player.getName());
        } else if (this.plugin.getSettingsManager().isCompleteMode()) {
            out = plugin.formatComplete(plugin.getSettingsManager().getCompleteModeFormat(), player, clanPlayer);
        }

        if (out == null) {
            return;
        }

        event.setFormat(String.format(out, player.getDisplayName(), event.getMessage()));

        plugin.removeRetrievers(event.getRecipients(), clanPlayer, player);
    }
}
