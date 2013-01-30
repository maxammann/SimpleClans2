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

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.command.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.chat.Channel;
import com.p000ison.dev.simpleclans2.chat.SCChatLanguage;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a ClanChannelCommand
 */
public class ClanChannelCommand extends GenericPlayerCommand {

    private final SCCore sccore;

    public ClanChannelCommand(String name, SCCore sccore) {
        super(name);
        this.sccore = sccore;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer clanPlayer = sccore.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + sccore.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        String action = args[0];

        if (action.equalsIgnoreCase("join")) {
            SimpleClansChat.setChannel(clanPlayer, Channel.ALLY);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.joined"));
        } else if (action.equalsIgnoreCase("leave")) {
            SimpleClansChat.setChannel(clanPlayer, null);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.leaved"));
        } else if (action.equalsIgnoreCase("on")) {
            SimpleClansChat.addDisabledChannel(clanPlayer, Channel.ALLY, true);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.on"));
        } else if (action.equalsIgnoreCase("off")) {
            SimpleClansChat.addDisabledChannel(clanPlayer, Channel.ALLY, false);
            ChatBlock.sendMessage(player, ChatColor.AQUA + SCChatLanguage.getTranslation("clan.channel.off"));
        }
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer) {
        return null;
    }
}
