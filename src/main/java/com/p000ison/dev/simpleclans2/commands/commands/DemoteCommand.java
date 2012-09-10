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
 *     Created: 10.09.12 14:44
 */

package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.util.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a DemoteCommand
 */
public class DemoteCommand extends GenericPlayerCommand {

    public DemoteCommand(SimpleClans plugin)
    {
        super("Demote", plugin);
        this.plugin = plugin;
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.demote"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("demote.command"));
        setPermission("simpleclans.leader.demote");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            return MessageFormat.format(Language.getTranslation("menu.demote"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isLeader(cp)) {
                ClanPlayer demoted = plugin.getClanPlayerManager().getClanPlayer(args[0]);

                if (!clan.allLeadersOnline(demoted)) {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("leaders.must.be.online.to.vote.on.demotion"));
                    return;
                }

                if (!clan.isLeader(demoted)) {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("player.is.not.a.leader.of.your.clan"));
                    return;
                }

                if (!plugin.getSettingsManager().isVoteForDemote()) {
                    clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("demoted.back.to.member"), demoted));
                    clan.demote(demoted);
                } else {
//                    plugin.getRequestManager().createRequest(cp, demoted, clan);
                    ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("demotion.vote.has.been.requested.from.all.leaders"));
                }

            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}