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
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CoordsCommand
 */
public class CoordsCommand extends GenericPlayerCommand {

    public CoordsCommand(SimpleClans plugin) {
        super("Coords", plugin);
        setDescription(Language.getTranslation("description.coords"));
        setIdentifiers(Language.getTranslation("coords.command"));
        addPermission("simpleclans.member.coords");

        setNeedsClan();
        setNeedsClanVerified();
        setNeedsTrusted();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();
        ChatColor subColor = getPlugin().getSettingsManager().getSubPageColor();

        Clan clan = cp.getClan();

        List<ClanPlayer> members = new ArrayList<ClanPlayer>(GeneralHelper.stripOfflinePlayers(clan.getMembers()));

        if (members.isEmpty()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.are.the.only.member.online"));
            return;
        }

        int completeSize = members.size();

        ChatBlock chatBlock = new ChatBlock();

        chatBlock.setAlignment(Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER);

        chatBlock.addRow(headColor + Language.getTranslation("name"), Language.getTranslation("distance"), Language.getTranslation("coords.upper"), Language.getTranslation("world"));

        int page = info.getPage(completeSize);
        int start = info.getStartIndex(page, completeSize);
        int end = info.getEndIndex(page, completeSize);

        for (int i = start; i < end; i++) {
            ClanPlayer clanPlayer = members.get(i);
            Player iPlayer = clanPlayer.toPlayer();

            if (iPlayer == null) {
                continue;
            }


            String name = clanPlayer.getColor() + clanPlayer.getName();
            Location loc = iPlayer.getLocation();
            int distance = (int) Math.ceil(loc.toVector().distance(player.getLocation().toVector()));
            String coords = loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
            String world = loc.getWorld().getName();

            chatBlock.addRow(name, ChatColor.AQUA.toString() + distance, ChatColor.WHITE.toString() + coords, world);
        }


        ChatBlock.sendBlank(player);
        ChatBlock.sendSingle(player, getPlugin().getSettingsManager().getClanColor() + clan.getName() + subColor + " " + Language.getTranslation("coords"));
        ChatBlock.sendBlank(player);


        chatBlock.sendBlock(player);
    }
}