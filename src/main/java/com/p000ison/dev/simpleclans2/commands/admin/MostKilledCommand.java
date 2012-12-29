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
 *     Last modified: 04.11.12 00:52
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.database.response.responses.MostKilledResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a MostKilledCommand
 */
public class MostKilledCommand extends GenericConsoleCommand {

    public MostKilledCommand(SimpleClans plugin)
    {
        super("MostKilled", plugin);
        setArgumentRange(0, 0);
        setUsages(Language.getTranslation("usage.mostkilled", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("mostkilled.command"));
        setPermission("simpleclans.mod.mostkilled");
    }

    @Override
    public String getMenu()
    {
        return Language.getTranslation("menu.mostkilled", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        int page = CommandManager.getPage(args);

        if (page == -1) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("number.format"));
            return;
        }

        plugin.getDataManager().addResponse(new MostKilledResponse(plugin, sender, page));
    }
}