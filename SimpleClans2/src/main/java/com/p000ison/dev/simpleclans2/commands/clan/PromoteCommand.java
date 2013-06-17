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
 *     Last modified: 11.10.12 15:51
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.PromoteRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a PromoteCommand
 */
public class PromoteCommand extends GenericPlayerCommand {

    public PromoteCommand(SimpleClans plugin) {
        super("Promote", plugin);
        addArgument(Language.getTranslation("argument.member"));
        setDescription(Language.getTranslation("description.promote"));
        setIdentifiers(Language.getTranslation("promote.command"));
        addPermission("simpleclans.leader.promote");

        setNeedsClan();
        setNeedsLeader();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        Player promotedPlayer = Bukkit.getPlayer(arguments[0]);

        if (promotedPlayer == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.does.not.have.the.permissions.to.lead.a.clan"));
            return;
        }

        ClanPlayer promoted = getPlugin().getClanPlayerManager().getClanPlayer(promotedPlayer);


        if (promotedPlayer.equals(player)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.cannot.promote.yourself"));
            return;
        }

        if (!clan.isMember(promoted)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.is.not.a.member.of.your.clan"));
            return;
        }

        if (promoted.isLeader()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.is.already.a.leader"));
            return;
        }

        if (!promotedPlayer.hasPermission("simpleclans.leader.promotable")) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.member.to.be.promoted.must.be.online"));
            return;
        }

        if (!clan.allLeadersOnline()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("all.leaders.must.be.online.to.vote.on.this.promotion"));
            return;
        }

        if (!getPlugin().getSettingsManager().isVoteForPromote()) {
            clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("promoted.to.leader"), promotedPlayer.getName()));
            promoted.setLeader(true);
            promoted.update(true);
        } else {
            Set<ClanPlayer> acceptors = GeneralHelper.stripOfflinePlayers(clan.getLeaders());

            getPlugin().getRequestManager().createRequest(new PromoteRequest(getPlugin(), acceptors, cp, promoted));
            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("promote.vote.has.been.requested.from.all.leaders"));
        }
    }
}