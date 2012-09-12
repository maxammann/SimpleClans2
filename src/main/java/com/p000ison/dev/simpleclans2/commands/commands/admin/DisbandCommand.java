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
 *     Created: 11.09.12 19:46
 */

package com.p000ison.dev.simpleclans2.commands.commands.admin;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.util.Announcer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class DisbandCommand extends GenericConsoleCommand {

    public DisbandCommand(SimpleClans plugin)
    {
        super("Disband", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.disband"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("disband.command"));
        setPermission("simpleclans.mod.disband");
    }

    @Override
    public String getMenu()
    {
        return ChatColor.DARK_RED + MessageFormat.format(Language.getTranslation("menu.disband"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return;
            }

            if (sender.hasPermission("simpleclans.member.resign")) {
                ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayerExact(sender.getName());

                if (cp != null) {
                    Clan clan = cp.getClan();

                    if (!clan.isLeader(cp) || clan.getLeaders().size() > 1) {
                        clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("0.has.resigned"), sender.getName()));
                        clan.removeMember(cp);
                    } else if (clan.isLeader(cp) && clan.getLeaders().size() == 1) {
                        Announcer.announce(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
                        clan.disband();
                    } else {
                        sender.sendMessage(ChatColor.RED + Language.getTranslation("last.leader.cannot.resign.you.must.appoint.another.leader.or.disband.the.clan"));
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
                }
            } else {
                sender.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
            }
        } else if (args.length == 1) {
            Clan clan = plugin.getClanManager().getClan(args[0]);

            if (clan != null) {
                Announcer.announce(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
                clan.disband();
            } else {
                sender.sendMessage(Language.getTranslation("no.clan.matched"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("usage.disband"), plugin.getSettingsManager().getClanCommand()));
        }
    }
}
