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
 *     Last modified: 31.10.12 20:48
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.util.comparators.LastSeenComparator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a RosterCommand
 */
public class RosterCommand extends GenericPlayerCommand {

    public RosterCommand(SimpleClans plugin)
    {
        super("Roster", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.roster"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("roster.command"));
        setPermission("simpleclans.member.roster");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("menu.roster"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        int page = CommandManager.getPage(args);

        if (page == -1) {
            player.sendMessage(Language.getTranslation("number.format"));
            return;
        }

        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (clan.isVerified()) {
            ChatBlock chatBlock = new ChatBlock();

            ChatBlock.sendBlank(player);
            ChatBlock.sendHead(player, plugin.getSettingsManager().getClanColor() + clan.getName(), Language.getTranslation("roster"));
            ChatBlock.sendBlank(player);
            ChatBlock.sendMessage(player, headColor + Language.getTranslation("legend") + " " + plugin.getSettingsManager().getLeaderColor() + Language.getTranslation("leader") + headColor + ", " + plugin.getSettingsManager().getTrustedColor() + Language.getTranslation("trusted") + headColor + ", " + plugin.getSettingsManager().getUntrustedColor() + Language.getTranslation("untrusted"));
            ChatBlock.sendBlank(player);

            chatBlock.addRow(headColor + Language.getTranslation("player"), Language.getTranslation("rank"), Language.getTranslation("seen"));

            List<ClanPlayer> leaders = new ArrayList<ClanPlayer>(clan.getLeaders());
            Collections.sort(leaders, new LastSeenComparator());

            List<ClanPlayer> members = new ArrayList<ClanPlayer>(clan.getLeaders());
            Collections.sort(members, new LastSeenComparator());

            int[] boundings = getBoundings(leaders.size() + members.size(), page);

            int i = boundings[0];
            int end = boundings[1];

            for (; i < end && i < leaders.size(); i++) {
                chatBlock.addRow(leaders.get(i).getRosterRow());
            }

            for (; i < end && i < members.size(); i++) {
                chatBlock.addRow(members.get(i).getRosterRow());
            }

            chatBlock.sendBlock(player);

            ChatBlock.sendBlank(player);
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
        }
    }
}