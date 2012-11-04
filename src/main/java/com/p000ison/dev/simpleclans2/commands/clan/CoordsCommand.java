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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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
        setPermission("simpleclans.member.coords");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.getClan().isVerified() && cp.isTrusted()) {
            return MessageFormat.format(Language.getTranslation("menu.coords"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);


        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (cp.isTrusted()) {

                    List<ClanPlayer> members = new ArrayList<ClanPlayer>(GeneralHelper.stripOfflinePlayers(clan.getMembers()));

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
                            ChatBlock.sendMessage(player, Language.getTranslation("number.format"));
                            return;
                        }
                    }

                    ChatBlock chatBlock = new ChatBlock();

                    chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER);

                    chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("distance"), Language.getTranslation("coords.upper"), Language.getTranslation("world"));

                    int[] boundings = getBoundings(completeSize, page);

                    for (int i = boundings[0]; i < boundings[1]; i++) {
                        ClanPlayer clanPlayer = members.get(i);
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
                    ChatBlock.sendSingle(player, plugin.getSettingsManager().getClanColor() + clan.getName() + subColor + " " + Language.getTranslation("coords"));
                    ChatBlock.sendBlank(player);


                    chatBlock.sendBlock(player);


                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.coords"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }

        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}