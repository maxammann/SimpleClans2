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
import com.p000ison.dev.simpleclans2.chat.Channel;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("unused")
public class SCCPlayerListener implements Listener {

    private SimpleClansChat plugin;

    public SCCPlayerListener(SimpleClansChat plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public AsyncPlayerChatEvent onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        String out = null;

        if (this.plugin.getSettingsManager().isCompatibilityMode()) {
            out = plugin.formatCompatibility(event.getFormat(), player.getName());
        } else if (this.plugin.getSettingsManager().isCompleteMode()) {
            out = plugin.formatComplete(plugin.getSettingsManager().getCompleteModeFormat(), player, event.getMessage());
        }

        if (out == null) {
            return event;
        }

        event.setFormat(out);

        //channels

        if (cp == null || cp.getFlags() == null) {
            return event;
        }

        byte flag = cp.getFlags().getByte("channel");

        if (flag == -1) {
            return event;
        }

        Channel channel = Channel.getById(flag);

        switch (channel) {
            case ALLY:
                //format for allies
                String formattedAlly = plugin.formatComplete(plugin.getSettingsManager().getAllyChannelFormat(), player, event.getMessage());

                for (Player other : plugin.getServer().getOnlinePlayers()) {
                    if (!isChannelDisabled(player, Channel.ALLY)) {
                        other.sendMessage(formattedAlly);
                    }
                }

                event.setCancelled(true);
                return event;
            case CLAN:
                //format for clan

                String formattedClan = plugin.formatComplete(plugin.getSettingsManager().getClanChannelFormat(), player, event.getMessage());

                for (Player other : plugin.getServer().getOnlinePlayers()) {
                    if (!isChannelDisabled(player, Channel.CLAN)) {
                        other.sendMessage(formattedClan);
                    }
                }

                event.setCancelled(true);
                return event;
        }


        for (Player other : plugin.getServer().getOnlinePlayers()) {
            if (!isChannelDisabled(player, Channel.GLOBAL)) {
                other.sendMessage(event.getFormat());
            }
        }

        event.setCancelled(true);
        return event;
    }

    private boolean isChannelDisabled(Player player, Channel channel) {
        return isChannelDisabled(plugin.getClanPlayerManager().getClanPlayer(player), channel);
    }

    private boolean isChannelDisabled(ClanPlayer player, Channel channel) {
        return player != null && player.getFlags() != null && player.getFlags().<Byte>getSet("disabledChannels").contains(channel.getId());
    }
}