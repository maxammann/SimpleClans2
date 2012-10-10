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
 *     Created: 03.10.12 19:52
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankSetCommand
 */
public class RankAddPermissionCommand extends GenericPlayerCommand {


    public RankAddPermissionCommand(SimpleClans plugin)
    {
        super("RankAddPermission", plugin);
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.rank.add.permission", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("rank.add.permission.command"));
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        if (clanPlayer != null) {
            if (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ranks")) {
                return Language.getTranslation("menu.rank.add.permission", plugin.getSettingsManager().getClanCommand());
            }
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

        Clan clan = clanPlayer.getClan();

        if (!clan.isLeader(clanPlayer) && !clanPlayer.hasRankPermission("manage.ranks")) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        Rank rank = clan.getRank(GeneralHelper.arrayBoundsToString(1, args));

        if (rank == null) {
            player.sendMessage(Language.getTranslation("rank.not.found"));
            return;
        }

        String added = rank.addPermission(args[0]);

        if (added == null) {
            player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("permission.not.found"));
            return;
        }

        rank.update(true);

        player.sendMessage(ChatColor.AQUA + Language.getTranslation("rank.permission.added", added, rank.getName()));
    }
}
