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
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.DemoteRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a DemoteCommand
 */
public class DemoteCommand extends GenericPlayerCommand {

    public DemoteCommand(SimpleClans plugin) {
        super("Demote", plugin);
        addArgument(Language.getTranslation("argument.leader"));
        setDescription(Language.getTranslation("description.demote"));
        setIdentifiers(Language.getTranslation("demote.command"));
        addPermission("simpleclans.leader.demote");

        setNeedsClan();
        setRankPermission("leader.demote");
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        ClanPlayer demoted = getPlugin().getClanPlayerManager().getClanPlayer(arguments[0]);

        if (!clan.allLeadersOnline(demoted)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("leaders.must.be.online.to.vote.on.demotion"));
            return;
        }

        if (!clan.isLeader(demoted)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("player.is.not.a.leader.of.your.clan"));
            return;
        }

        if (!getPlugin().getSettingsManager().isVoteForDemote()) {
            clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("demoted.back.to.member"), demoted.getName()));
            clan.demote(demoted);
        } else {
            Set<ClanPlayer> acceptors = GeneralHelper.stripOfflinePlayers(clan.getLeaders());
            acceptors.remove(demoted);

            getPlugin().getRequestManager().createRequest(new DemoteRequest(getPlugin(), acceptors, cp, demoted));
            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("demotion.vote.has.been.requested.from.all.leaders"));
        }
    }
}