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
 *     Last modified: 10/20/12 5:08 PM
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.database.statements.RemoveRankStatement;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankCreateCommand
 */
public class RankDeleteCommand extends GenericPlayerCommand {

    public RankDeleteCommand(SimpleClans plugin) {
        super("RankDelete", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.rank.delete", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("rank.delete.command"));
        setPermission("simpleclans.leader.rank.delete");
        setType(Type.RANK);
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer) {
        if (clanPlayer != null && (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ranks"))) {
            return Language.getTranslation("menu.rank.delete", plugin.getSettingsManager().getRankCommand());
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

        long response = clan.deleteRank(args[0]);

        if (response == -1) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("rank.not.found"));
            return;
        }

        plugin.getDataManager().addStatement(new RemoveRankStatement(response));

        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("rank.deleted"));
    }
}
