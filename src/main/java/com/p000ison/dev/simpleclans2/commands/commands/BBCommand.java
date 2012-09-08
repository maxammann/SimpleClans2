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
 *     Created: 07.09.12 02:04
 */

package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.util.ChatBlock;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a BBCommand
 */
public class BBCommand extends GenericPlayerCommand {


    public BBCommand(SimpleClans plugin)
    {
        super("BB", plugin);
        setArgumentRange(0, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bb"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("bb.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp, CommandSender sender)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                String display = null;
                if (sender.hasPermission("simpleclans.member.bb")) {
                    display = MessageFormat.format(Language.getTranslation("0.bb.1.display.bulletin.board"), plugin.getSettingsManager().getClanCommand()) + "\n   §b";
                }
                if (sender.hasPermission("simpleclans.member.bb-add")) {
                    display += MessageFormat.format(Language.getTranslation("0.bb.msg.1.add.a.message.to.the.bulletin.board"), plugin.getSettingsManager().getClanCommand());
                }
                return display;
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        if (args.length == 0) {

            if (player.hasPermission("simpleclans.member.bb")) {
                clan.displayBb(player, plugin.getSettingsManager().getMaxBBDisplayLines());
            } else {
                player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("insufficient.permissions"));
            }

        } else if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {

            if (player.hasPermission("simpleclans.leader.bb-clear")) {
                if (cp.isTrusted() && cp.isLeader()) {
                    cp.getClan().clearBB();
//                            plugin.getDataManager().updateClan(clan);
                    player.sendMessage(ChatColor.RED + Language.getTranslation("cleared.bb"));
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
            }

        } else if (player.hasPermission("simpleclans.member.bb-add")) {

            if (cp.isTrusted()) {
                String msg = GeneralHelper.arrayToStringFromStart(1, args);
                clan.addBBMessage(cp, msg);
//                        plugin.getDataManager().updateClan(clan);
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            }

        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }
}
