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
 *     Created: 08.09.12 13:36
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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a CoordsCommand
 */
public class CoordsCommand extends GenericPlayerCommand {

    public CoordsCommand(SimpleClans plugin)
    {
        super("Coords", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.coords"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("coords.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp, CommandSender sender)
    {
        if (cp != null) {
            if (cp.getClan().isVerified() && cp.isTrusted() && sender.hasPermission("simpleclans.member.coords")) {
                return MessageFormat.format(Language.getTranslation("menu.coords"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeadingPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();

        if (player.hasPermission("simpleclans.member.coords")) {
            ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);


            if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isVerified()) {
                    if (cp.isTrusted()) {

                        Set<ClanPlayer> members = GeneralHelper.stripOfflinePlayers(clan.getMembers());

                        if (members.isEmpty()) {
                            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.are.the.only.member.online"));
                            return;
                        }

                        int page = 0;
                        int completeSize = members.size();

                        if (args.length == 1) {
                            try {
                                page = Integer.parseInt(args[0]) - 1;
                            } catch (NumberFormatException e) {
                                player.sendMessage(Language.getTranslation("number.format"));
                            }
                        }

                        ChatBlock chatBlock = new ChatBlock();

                        chatBlock.setAlignment("l", "c", "c", "c");

                        chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("distance"), Language.getTranslation("coords.upper"), Language.getTranslation("world"));




                        for (ClanPlayer clanPlayer : members) {
                            Player iPlayer = clanPlayer.toPlayer();

                            if (iPlayer == null) {
                                continue;
                            }


                            String name = (clanPlayer.isLeader() ? plugin.getSettingsManager().getLeaderColor() : ((clanPlayer.isTrusted() ? plugin.getSettingsManager().getTrustedColor() : plugin.getSettingsManager().getUntrustedColor()))) + clanPlayer.getName();
                            Location loc = iPlayer.getLocation();
                            int distance = (int) Math.ceil(loc.toVector().distance(player.getLocation().toVector()));
                            String coords = loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
                            String world = loc.getWorld().getName();

                            chatBlock.addRow(name, ChatColor.AQUA.toString() + distance, ChatColor.WHITE.toString() + coords, world);
                        }


                        ChatBlock.sendBlank(player);
                        ChatBlock.saySingle(player, plugin.getSettingsManager().getClanColor() + clan.getName() + subColor + " " + Language.getTranslation("coords") + " ");
                        ChatBlock.sendBlank(player);

                        int[] boundings = getBoundings(completeSize, page);

                        chatBlock.sendBlock(player, boundings[0], boundings[1]);



                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.coords"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                }

            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }
}