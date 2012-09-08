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
 *     Created: 05.09.12 13:00
 */

package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.requests.requests.AllyCreateRequest;
import com.p000ison.dev.simpleclans2.util.ChatBlock;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a AllyCommand
 */
public class AllyCommand extends GenericPlayerCommand {
    public AllyCommand(SimpleClans plugin)
    {
        super("Ally", plugin);
        this.plugin = plugin;
        setArgumentRange(2, 2);
        setUsages(MessageFormat.format(Language.getTranslation("usage.ally"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("ally.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer, CommandSender sender)
    {

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();
            if (clan != null) {
                if (sender.hasPermission("simpleclans.leader.ally")) {
                    if (clanPlayer.isLeader() && clan.isVerified()) {
                        return MessageFormat.format(Language.getTranslation("menu.ally"), plugin.getSettingsManager().getClanCommand());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
        if (player.hasPermission("simpleclans.leader.ally")) {
            ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();

                if (!clan.isVerified()) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                    return;
                }

                if (!clan.isLeader(cp)) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                    return;
                }

                if (clan.getSize() <= plugin.getSettingsManager().getMinimalSizeToAlly()) {
                    player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("minimum.to.make.alliance"), plugin.getSettingsManager().getMinimalSizeToAlly()));
                    return;
                }

                String action = args[0];
                Clan ally = plugin.getClanManager().getClan(args[1]);

                if (ally == null) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
                    return;
                }

                if (!ally.isVerified()) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("cannot.ally.with.an.unverified.clan"));
                    return;
                }

                if (action.equals(Language.getTranslation("add"))) {
                    if (!clan.isAlly(ally)) {

                        Set<ClanPlayer> leaders = clan.getLeaders();
                        GeneralHelper.stripOfflinePlayers(leaders);

                        if (!leaders.isEmpty()) {
                            plugin.getRequestManager().createRequest(new AllyCreateRequest(leaders, cp, clan, "", ally));
                            player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("leaders.have.been.asked.for.an.alliance"), ally.getName()));
                        } else {
                            player.sendMessage(ChatColor.RED + Language.getTranslation("at.least.one.leader.accept.the.alliance"));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + Language.getTranslation("your.clans.are.already.allies"));
                    }
                } else if (action.equals(Language.getTranslation("remove"))) {
                    if (clan.isAlly(ally)) {
                        clan.removeAlly(ally);
                        ally.addBBMessage(cp.getName(), ChatColor.AQUA + MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), clan.getName(), ally.getName()));
                        clan.addBBMessage(cp.getName(), ChatColor.AQUA + MessageFormat.format(Language.getTranslation("has.broken.the.alliance"), cp.getName(), ally.getName()));
                    } else {
                        player.sendMessage(ChatColor.RED + Language.getTranslation("your.clans.are.not.allies"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("usage.ally"), plugin.getSettingsManager().getClanCommand()));
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }
}
