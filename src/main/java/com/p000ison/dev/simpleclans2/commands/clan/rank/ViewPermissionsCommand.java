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
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Represents a ViewPermissionsCommand
 */
public class ViewPermissionsCommand extends GenericConsoleCommand {

    public ViewPermissionsCommand(SimpleClans plugin)
    {
        super("ViewPermissions", plugin);
        setArgumentRange(0, 0);
        setUsages(Language.getTranslation("usage.view.permissions", plugin.getSettingsManager().getRankCommand()));
        setIdentifiers(Language.getTranslation("view.permissions.command"));
        setPermission("simpleclans.rank.view.permissions");
        setType(Type.RANK);
    }

    @Override
    public String getMenu()
    {
        return Language.getTranslation("menu.rank.permissions", plugin.getSettingsManager().getRankCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendHead(sender, Language.getTranslation("global"), Language.getTranslation("permissions"));

        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment(Align.CENTER, Align.LEFT);
        chatBlock.addRow(ChatBlock.getHeadingColor() + "ID", ChatBlock.getHeadingColor() + Language.getTranslation("permission"));

        for (Map.Entry<Integer, String> entry : Rank.getAvailablePermissions().entrySet()) {
            chatBlock.addRow(ChatColor.GRAY.toString() + entry.getKey(), ChatColor.GRAY + entry.getValue());
        }

        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender, 2);
    }
}
