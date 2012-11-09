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
 *     Last modified: 04.11.12 15:24
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * Represents a ConvertCommand
 */
public class UpgradeCommand extends GenericConsoleCommand {

    public UpgradeCommand(SimpleClans plugin)
    {
        super("UpgradeCommand", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.upgrade"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("upgrade.command"));
        setPermission("simpleclans.admin.upgrade");
    }

    @Override
    public String getMenu()
    {
        return Language.getTranslation("menu.upgrade", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (!plugin.isUpdate()) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("no.upgrade"));
            return;
        }

        if (!plugin.update()) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("upgrade.failed"));
            return;
        }

        ChatBlock.sendMessage(sender, ChatColor.GREEN + Language.getTranslation("upgrade.successfully"));
    }
}
