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

/**
 * Represents a RankSetCommand
 */
public class RankAddPermissionCommand extends GenericPlayerCommand {


    public RankAddPermissionCommand(SimpleClans plugin)
    {
        super("RankAddPermission", plugin);
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.rank.add.permission", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("rank.add.permission.command"));
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        if (clanPlayer != null && (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ranks"))) {
            return Language.getTranslation("menu.rank.add.permission", plugin.getSettingsManager().getRankCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (!clan.isLeader(clanPlayer) && !clanPlayer.hasRankPermission("manage.ranks")) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        Rank rank = clan.getRank(args[0]);

        if (rank == null) {
            ChatBlock.sendMessage(player, Language.getTranslation("rank.not.found"));
            return;
        }

        String permission = args[1];
        boolean positive = true;

        if (permission.charAt(0) == '-') {
            positive = false;
        }

        String added = rank.addPermission(permission.substring(1), positive);

        if (added == null) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("permission.not.found"));
            return;
        }

        rank.update(true);
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("rank.permission.added", added, rank.getName()));
    }
}
