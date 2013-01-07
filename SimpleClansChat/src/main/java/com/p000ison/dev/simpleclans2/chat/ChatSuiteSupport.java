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

package com.p000ison.dev.simpleclans2.chat;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import net.krinsoft.chat.api.Target;
import net.krinsoft.chat.targets.Channel;
import net.krinsoft.chat.targets.ChatPlayer;
import org.bukkit.entity.Player;

/**
 * Represents a ChatSuiteSupport
 */
public class ChatSuiteSupport {

    private SimpleClansChat plugin;

    public ChatSuiteSupport(SimpleClansChat plugin)
    {
        this.plugin = plugin;
    }

    public void inviteClanMembers(Player player)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);
        ChatPlayer chatPlayer = plugin.getChatSuite().getPlayerManager().getPlayer(player.getName());

        Target target = chatPlayer.getTarget();

        if (target instanceof Channel) {
            Channel channel = (Channel) target;
            for (ClanPlayer cp : clanPlayer.getClan().getAllMembers()) {
                channel.invite(player, cp.toPlayer());
            }
        }
    }
}
