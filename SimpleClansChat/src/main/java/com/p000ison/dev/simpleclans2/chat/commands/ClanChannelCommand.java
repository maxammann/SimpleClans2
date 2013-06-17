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
 *     Last modified: 1/28/13 5:52 PM
 */

package com.p000ison.dev.simpleclans2.chat.commands;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.chat.Channel;
import com.p000ison.dev.simpleclans2.chat.SCChatLanguage;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a ClanChannelCommand
 */
public class ClanChannelCommand extends GenericPlayerCommand {

    public ClanChannelCommand(String name, SCCore sccore) {
        super(name, sccore);
//        setArgumentRange(1, 1);
        setDescription(SCChatLanguage.getTranslation("usage.clan"));
        setIdentifiers(SCChatLanguage.getTranslation("clan.chat.command"));
        addPermission("simpleclans.member.channels.clan");

        setNeedsClan();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        String action = arguments[0];

        if (action.equalsIgnoreCase("join")) {
            SimpleClansChat.setChannel(cp, Channel.CLAN);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.joined"));
        } else if (action.equalsIgnoreCase("leave")) {
            SimpleClansChat.setChannel(cp, null);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.leaved"));
        } else if (action.equalsIgnoreCase("on")) {
            SimpleClansChat.addDisabledChannel(cp, Channel.CLAN, true);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.on"));
        } else if (action.equalsIgnoreCase("off")) {
            SimpleClansChat.addDisabledChannel(cp, Channel.CLAN, false);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.off"));
        }
    }
}
