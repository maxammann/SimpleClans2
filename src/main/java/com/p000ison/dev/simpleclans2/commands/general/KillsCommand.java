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
 *     Last modified: 01.11.12 22:17
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.database.data.response.responses.KillsResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a KillsCommand
 */
public class KillsCommand extends GenericPlayerCommand {

    public KillsCommand(SimpleClans plugin)
    {
        super("Kills", plugin);
        setArgumentRange(0, 1);
        setUsages(Language.getTranslation("usage.kills", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("kills.command"));
        setPermission("simpleclans.member.kills");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        return Language.getTranslation("menu.kills", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        int page = CommandManager.getPage(args);

        if (page == -1) {
            ChatBlock.sendMessage(player, ChatColor.DARK_RED + Language.getTranslation("number.format"));
            return;
        }

        plugin.getDataManager().addResponse(new KillsResponse(plugin, player, cp, page));
    }
}