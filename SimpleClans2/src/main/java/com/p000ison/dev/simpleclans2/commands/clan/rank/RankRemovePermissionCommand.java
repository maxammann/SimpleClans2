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
 *     Last modified: 16.11.12 19:57
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankAssignCommand
 */
public class RankRemovePermissionCommand extends GenericPlayerCommand {


    public RankRemovePermissionCommand(SimpleClans plugin) {
        super("RankRemovePermission", plugin);
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.rank.remove.permission", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("rank.remove.permission.command"));
        setPermission("simpleclans.leader.rank.permissions.remove");
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer) {
        if (clanPlayer != null && (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ranks"))) {
            return Language.getTranslation("menu.rank.remove.permission", plugin.getSettingsManager().getRankCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
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
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("rank.not.found"));
            return;
        }

        String permission = args[1];

        String removed = rank.removePermission(permission);

        if (removed == null) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("permission.not.found"));
            return;
        }

        rank.update(true);
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("rank.permission.removed", removed, rank.getName()));
    }
}
