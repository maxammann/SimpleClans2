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

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankCreateCommand
 */
public class RankCreateCommand extends GenericPlayerCommand {
    public RankCreateCommand(SimpleClans plugin) {
        super("Create rank", plugin);
        addArgument(Language.getTranslation("argument.tag")).
                addArgument(Language.getTranslation("argument.name"));
        setInfinite(true);
        setDescription(Language.getTranslation("description.rank.create", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("rank.create.command"));
        addPermission("simpleclans.leader.rank.create");

        setNeedsClan();
        setRankPermission("manage.ranks");
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        int priority;

        try {
            priority = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            return;
        }

        String name = ChatBlock.parseColors(GeneralHelper.arrayBoundsToString(2, arguments));
        String tag = ChatBlock.parseColors(arguments[1]);

        if (!getPlugin().getRankManager().verifyRankTag(player, tag)) {
            return;
        }

        if (!getPlugin().getRankManager().verifyRankName(player, name)) {
            return;
        }

        if (clan.getRank(tag) != null || clan.getRankByName(name) != null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("rank.exists.already"));
            return;
        }

        clan.addRank(getPlugin().getRankManager().createRank(clan, name, tag, priority));
        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("rank.created", name));
    }
}
