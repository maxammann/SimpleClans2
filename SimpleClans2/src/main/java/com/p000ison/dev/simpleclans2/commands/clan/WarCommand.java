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
 *     Last modified: 02.11.12 16:54
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.WarStartRequest;
import com.p000ison.dev.simpleclans2.requests.requests.WarStopRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a WarCommand
 */
public class WarCommand extends GenericPlayerCommand {

    private SimpleClans plugin;

    public WarCommand(SimpleClans plugin)
    {
        super("War", plugin);
        this.plugin = plugin;
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.war", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("war.command"));
        setPermission("simpleclans.leader.war");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.isLeader() && cp.getClan().isVerified()) {
                return Language.getTranslation("menu.war", plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        if (!clan.isLeader(cp)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        String action = args[0];
        Clan toWarring = plugin.getClanManager().getClan(args[1]);

        if (toWarring == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (!clan.isRival(toWarring)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.can.only.start.war.with.rivals"));
            return;
        }

        if (action.equalsIgnoreCase(Language.getTranslation("start"))) {
            if (!clan.isWarring(toWarring)) {
                Set<ClanPlayer> onlineLeaders = GeneralHelper.stripOfflinePlayers(toWarring.getLeaders());

                if (!onlineLeaders.isEmpty()) {
                    plugin.getRequestManager().createRequest(new WarStartRequest(plugin, onlineLeaders, cp, toWarring));
                    ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("leaders.have.been.asked.to.accept.the.war.request", toWarring.getName()));
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.war.start"));
                }

            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clans.already.at.war"));
            }
        } else if (action.equalsIgnoreCase(Language.getTranslation("end"))) {
            if (clan.isWarring(toWarring)) {

                Set<ClanPlayer> onlineLeaders = GeneralHelper.stripOfflinePlayers(toWarring.getLeaders());
                if (!onlineLeaders.isEmpty()) {
                    plugin.getRequestManager().createRequest(new WarStopRequest(plugin, onlineLeaders, cp, toWarring));
                    ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("leaders.asked.to.end.rivalry", toWarring.getName()));
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.war.stop"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clans.not.at.war"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("usage.war", plugin.getSettingsManager().getClanCommand()));
        }
    }
}