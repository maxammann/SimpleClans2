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

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a RankCreateCommand
 */
public class RankCreateCommand extends GenericPlayerCommand {
    public RankCreateCommand(SimpleClans plugin)
    {
        super("RankCreate", plugin);
        setArgumentRange(2, 2);
        setUsages(MessageFormat.format(Language.getTranslation("usage.create.ranks"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("ranks.create.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (clan == null) {
            return;
        }

        if (!clan.isLeader(clanPlayer) && !clanPlayer.hasPermission("manage.ranks")) {
            return;
        }

        int priority;

        try {
            priority = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return;
        }

        plugin.getRankManager().createRank(clan, GeneralHelper.arrayBoundsToString(1, args), priority);
    }
}
