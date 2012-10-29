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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
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

    public StatsCommand(SimpleClans plugin)
    {
        super("Stats", plugin);
        setArgumentRange(0, 0);
        setUsages(Language.getTranslation("usage.stats", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("stats.command"));
        setPermission("simpleclans.member.stats");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.isTrusted() && cp.getClan().isVerified()) {
            return Language.getTranslation("menu.stats", plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();


        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (cp.isTrusted()) {

                    int page =  CommandManager.getPage(args);

                    if (page == -1) {
                        player.sendMessage(Language.getTranslation("number.format"));
                        return;
                    }

                    ChatBlock chatBlock = new ChatBlock();

                    ChatBlock.sendHead(player, plugin.getSettingsManager().getClanColor() + clan.getName(), Language.getTranslation("stats"));
                    ChatBlock.sendBlank(player);

                    ChatBlock.sendMessage(player, headColor + Language.getTranslation("kdr") + " = " + subColor + Language.getTranslation("kill.death.ratio"));
                    ChatBlock.sendMessage(player, headColor + Language.getTranslation("weights") + " = " + Language.getTranslation("rival") + ": " + subColor + plugin.getSettingsManager().getKillWeightRival() + headColor + " " + Language.getTranslation("neutral") + ": " + subColor + plugin.getSettingsManager().getKillWeightNeutral() + headColor + " " + Language.getTranslation("civilian") + ": " + subColor + plugin.getSettingsManager().getKillWeightCivilian());
                    ChatBlock.sendBlank(player);

                    chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER, Align.CENTER, Align.CENTER);

                    chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("kdr"), Language.getTranslation("rival"), Language.getTranslation("neutral"), Language.getTranslation("civilian.abbreviation"), Language.getTranslation("deaths"));

                    List<ClanPlayer> leaders = new ArrayList<ClanPlayer>(clan.getLeaders());
                    Collections.sort(leaders, new KDRComparator());

                    List<ClanPlayer> members = new ArrayList<ClanPlayer>(clan.getMembers());
                    Collections.sort(members, new KDRComparator());


                    int[] boundings = getBoundings(leaders.size() + members.size(), page);

                    int i = boundings[0];
                    int end = boundings[1];

                    for (; i < end && i < leaders.size(); i++) {
                        chatBlock.addRow(leaders.get(i).getStatisticRow());
                    }

                    for (; i < end && i < members.size(); i++) {
                        chatBlock.addRow(members.get(i).getStatisticRow());
                    }

                    chatBlock.sendBlock(player);

                    ChatBlock.sendBlank(player);
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.stats"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}