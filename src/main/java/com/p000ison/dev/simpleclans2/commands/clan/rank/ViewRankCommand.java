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
 *     Last modified: 31.10.12 23:54
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a ViewPermissionsCommand
 */
public class ViewRankCommand extends GenericPlayerCommand {

    public ViewRankCommand(SimpleClans plugin)
    {
        super("ViewRank", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.view.rank", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("view.rank.command"));
        setPermission("simpleclans.rank.view.rank");
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        if (clanPlayer != null) {
            return Language.getTranslation("menu.rank.view", plugin.getSettingsManager().getRankCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        if (!clanPlayer.isLeader() && !clanPlayer.hasRankPermission("manage.ranks")) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }
        Clan clan = clanPlayer.getClan();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();

        Rank queried;
        String query = args[0];

        try {
            long id = Long.parseLong(query);
            queried = clan.getRank(id);
        } catch (NumberFormatException e) {
            queried = clan.getRank(query);
        }

        if (queried == null) {
            player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("rank.not.found"));
            return;
        }

        ChatBlock.sendBlank(player);
        ChatBlock.sendHead(player, plugin.getSettingsManager().getClanColor() + Language.getTranslation("rank.head", clan.getTag()), null);

        ChatBlock.sendBlank(player, 2);


        String id = String.valueOf(queried.getId());
        String tag = queried.getTag();
        String name = queried.getName();

        player.sendMessage(subColor + Language.getTranslation("id") + ": " + ChatColor.WHITE + id);
        player.sendMessage(subColor + Language.getTranslation("tag") + ": " + ChatColor.WHITE + tag);
        player.sendMessage(subColor + Language.getTranslation("name") + ": " + ChatColor.WHITE + name);


        Set<Integer> permissions = queried.getPermissions();

        if (!permissions.isEmpty()) {
            player.sendMessage(subColor + Language.getTranslation("permissions") + ":");
            for (Integer permissionID : permissions) {
                player.sendMessage(subColor + "  - " + ChatColor.WHITE + Rank.getAvailablePermissions().get(permissionID));
            }
        }
    }
}
