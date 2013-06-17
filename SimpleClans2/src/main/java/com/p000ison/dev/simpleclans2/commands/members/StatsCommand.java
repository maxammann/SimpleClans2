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
 *     Last modified: 28.10.12 13:34
 */

package com.p000ison.dev.simpleclans2.commands.members;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.comparators.KDRComparator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a StatsCommand
 */
public class StatsCommand extends GenericPlayerCommand {

    public StatsCommand(SimpleClans plugin) {
        super("Stats", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(Language.getTranslation("description.stats"));
        setIdentifiers(Language.getTranslation("stats.command"));
        addPermission("simpleclans.member.stats");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsTrusted();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();
        ChatColor subColor = getPlugin().getSettingsManager().getSubPageColor();

        Clan clan = cp.getClan();

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(player);

        ChatBlock.sendMessage(player, headColor + Language.getTranslation("kdr") + " = " + subColor + Language.getTranslation("kill.death.ratio"));
        ChatBlock.sendMessage(player, headColor + Language.getTranslation("weights") + " = " + Language.getTranslation("rival") + ": " + subColor + getPlugin().getSettingsManager().getKillWeightRival() + headColor + " " + Language.getTranslation("neutral") + ": " + subColor + getPlugin().getSettingsManager().getKillWeightNeutral() + headColor + " " + Language.getTranslation("civilian") + ": " + subColor + getPlugin().getSettingsManager().getKillWeightCivilian());
        ChatBlock.sendBlank(player);

        chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER, Align.CENTER, Align.CENTER);

        chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("kdr"), Language.getTranslation("rival"), Language.getTranslation("neutral"), Language.getTranslation("civilian.abbreviation"), Language.getTranslation("deaths"));

        List<ClanPlayer> leaders = new ArrayList<ClanPlayer>(clan.getLeaders());
        Collections.sort(leaders, new KDRComparator());

        List<ClanPlayer> members = new ArrayList<ClanPlayer>(clan.getMembers());
        Collections.sort(members, new KDRComparator());


        int page = info.getPage(leaders.size() + members.size());
        int start = info.getStartIndex(page, leaders.size() + members.size());
        int end = info.getEndIndex(page, leaders.size() + members.size());

        int i = start;

        for (; i < end && i < leaders.size(); i++) {
            chatBlock.addRow(leaders.get(i).getStatisticRow());
        }

        i = 0;

        for (; i < end && i < members.size(); i++) {
            chatBlock.addRow(members.get(i).getStatisticRow());
        }

        chatBlock.sendBlock(player);

        ChatBlock.sendBlank(player);
    }
}