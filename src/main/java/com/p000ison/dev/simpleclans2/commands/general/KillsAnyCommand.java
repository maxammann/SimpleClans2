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
 *     Last modified: 02.11.12 16:45
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.database.data.response.responses.KillsResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a KillsAnyCommand
 */
public class KillsAnyCommand extends GenericConsoleCommand {

    public KillsAnyCommand(SimpleClans plugin)
    {
        super("KillsAnyone", plugin);
        setArgumentRange(1, 2);
        setUsages(Language.getTranslation("usage.kills.any", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("kills.command"));
        setPermission("simpleclans.anyone.kills");
    }

    @Override
    public String getMenu()
    {
        return Language.getTranslation("menu.kills.any", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getAnyClanPlayer(args[0]);

        if (cp == null) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("no.player.matched"));
            return;
        }

        int page = 0;
        if (args.length == 2) {
            page = CommandManager.getPage(args[1]);

            if (page == -1) {
                ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("number.format"));
                return;
            }
        }

        plugin.getDataManager().addResponse(new KillsResponse(plugin, sender, cp, page));
    }
}