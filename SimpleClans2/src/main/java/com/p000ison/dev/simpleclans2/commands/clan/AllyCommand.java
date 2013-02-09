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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.AllyCreateRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a AllyCommand
 */
public class AllyCommand extends GenericPlayerCommand {
    public AllyCommand(SimpleClans plugin) {
        super("Ally", plugin);
        this.plugin = plugin;
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.ally"));
        setIdentifiers(Language.getTranslation("ally.command"));
        setPermission("simpleclans.leader.ally");
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer) {

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();
            if (clan != null && (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ally")) && clan.isVerified()) {
                return Language.getTranslation("menu.ally");
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isLeader(cp) && !cp.hasRankPermission("manage.ally")) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        if (!clan.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        if (clan.getSize() < plugin.getSettingsManager().getMinimalSizeToAlly()) {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(Language.getTranslation("minimum.to.make.alliance"), plugin.getSettingsManager().getMinimalSizeToAlly()));
            return;
        }

        String action = args[0];
        Clan ally = plugin.getClanManager().getClan(args[1]);

        if (ally == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (!ally.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("cannot.ally.with.an.unverified.clan"));
            return;
        }

        if (action.equalsIgnoreCase(Language.getTranslation("add"))) {
            if (!clan.isAlly(ally)) {

                Set<ClanPlayer> leaders = GeneralHelper.stripOfflinePlayers(ally.getLeaders());

                if (!leaders.isEmpty()) {
                    plugin.getRequestManager().createRequest(new AllyCreateRequest(plugin, leaders, cp, ally));
                    ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("leaders.have.been.asked.for.an.alliance"), ally.getName()));
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.alliance"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.already.allies"));
            }
        } else if (action.equalsIgnoreCase(Language.getTranslation("remove"))) {
            if (clan.isAlly(ally)) {
                clan.removeAlly(ally);
                ally.removeAlly(clan);
                ally.addBBMessage(cp, MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), clan.getName(), ally.getName()));
                clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), clan.getName(), ally.getName()));
                clan.update();
                ally.update();
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.not.allies"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("usage.ally"));
        }

    }
}
