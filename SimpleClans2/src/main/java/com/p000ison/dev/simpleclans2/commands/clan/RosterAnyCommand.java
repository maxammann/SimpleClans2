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
 *     Last modified: 31.10.12 21:39
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.comparators.LastSeenComparator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a RosterAnyCommand
 */
public class RosterAnyCommand extends GenericPlayerCommand {

    public RosterAnyCommand(SimpleClans plugin) {
        super("Roster (Any)", plugin);
        addArgument(Language.getTranslation("argument.tag")).
                addArgument(Language.getTranslation("argument.page"), false);
        setDescription(Language.getTranslation("description.roster"));
        setIdentifiers(Language.getTranslation("roster.command"));
        addPermission("simpleclans.member.roster");
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();

        Clan clan = getPlugin().getClanManager().getClan(arguments[0]);

        if (clan == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;

        }

        if (clan.isVerified()) {
            ChatBlock chatBlock = new ChatBlock();

            chatBlock.setAlignment(Align.LEFT, Align.LEFT, Align.LEFT);

            ChatBlock.sendMessage(player, Language.getTranslation("displaying.clan", clan.getTag()));
            ChatBlock.sendBlank(player);
            ChatBlock.sendMessage(player, headColor + Language.getTranslation("legend") + " " + getPlugin().getSettingsManager().getLeaderColor() + Language.getTranslation("leader") + headColor + ", " + getPlugin().getSettingsManager().getTrustedColor() + Language.getTranslation("trusted") + headColor + ", " + getPlugin().getSettingsManager().getUntrustedColor() + Language.getTranslation("untrusted"));
            ChatBlock.sendBlank(player);

            chatBlock.addRow("  " + headColor + Language.getTranslation("player"), Language.getTranslation("rank"), Language.getTranslation("seen"));


            List<ClanPlayer> leaders = new ArrayList<ClanPlayer>(clan.getLeaders());
            Collections.sort(leaders, new LastSeenComparator());

            List<ClanPlayer> members = new ArrayList<ClanPlayer>(clan.getMembers());
            Collections.sort(members, new LastSeenComparator());

            int page = info.getPage(leaders.size() + members.size());
            int start = info.getStartIndex(page, leaders.size() + members.size());
            int end = info.getEndIndex(page, leaders.size() + members.size());

            int i = start;

            for (; i < end && i < leaders.size(); i++) {
                chatBlock.addRow(leaders.get(i).getRosterRow());
            }

            i = 0;

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