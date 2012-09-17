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

package com.p000ison.dev.simpleclans2.commands.commands.temp;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;


public class InviteCommand extends GenericPlayerCommand {


    public InviteCommand(SimpleClans plugin)
    {
        super("Invite");
        this.plugin = plugin;
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.invite"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("invite.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp, CommandSender sender)
    {
        if (cp != null) {
            if (cp.isLeader() && sender.hasPermission("simpleclans.leader.invite")) {
                return MessageFormat.format(Language.getTranslation("0.invite.player.1.invite.a.player"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
        if (player.hasPermission("simpleclans.leader.invite")) {
            ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isLeader(player)) {
                    Player invited = Helper.matchOnePlayer(args[0]);

                    if (invited != null) {
                        if (plugin.getPermissionsManager().has(invited, "simpleclans.member.can-join")) {
                            if (!invited.getName().equals(player.getName())) {
                                if (!plugin.getSettingsManager().isBanned(player.getName())) {
                                    ClanPlayer cpInv = plugin.getClanPlayerManager().getClanPlayer(invited);

                                    if (cpInv == null) {
                                        if (plugin.getClanManager().purchaseInvite(player)) {
                                            plugin.getRequestManager().addInviteRequest(cp, invited.getName(), clan);
                                            player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("has.been.asked.to.join"), Helper.capitalize(invited.getName()), clan.getName()));
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + Language.getTranslation("the.player.is.already.member.of.another.clan"));
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + Language.getTranslation("this.player.is.banned.from.using.clan.commands"));
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + Language.getTranslation("you.cannot.invite.yourself"));
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + Language.getTranslation("the.player.doesn.t.not.have.the.permissions.to.join.clans"));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + Language.getTranslation("no.player.matched"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }
}
