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
 *     Created: 09.09.12 19:05
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankCreateCommand
 */
public class RankCreateCommand extends GenericPlayerCommand {
    public RankCreateCommand(SimpleClans plugin)
    {
        super("RankCreate", plugin);
        setArgumentRange(3, 50);
        setUsages(Language.getTranslation("usage.rank.create", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("rank.create.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        if (clanPlayer != null) {
            return Language.getTranslation("menu.rank.create", plugin.getSettingsManager().getClanCommand());
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

        int priority;

        try {
            priority = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return;
        }

        String name = GeneralHelper.arrayBoundsToString(2, args);

        clan.addRank(plugin.getRankManager().createRank(clan, name, args[1], priority));
        player.sendMessage(ChatColor.AQUA + Language.getTranslation("rank.created", name));
    }
}
