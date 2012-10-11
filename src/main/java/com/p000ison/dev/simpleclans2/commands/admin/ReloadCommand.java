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

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;


public class ReloadCommand extends GenericConsoleCommand {

    public ReloadCommand(SimpleClans plugin)
    {
        super("Reload", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.reload"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("reload.command"));
        setPermission("simpleclans.admin.reload");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.reload"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        long start = System.currentTimeMillis();

        plugin.getSettingsManager().reload();
        plugin.getDataManager().getAutoSaver().run();
        plugin.getDataManager().importAll();

        long end = System.currentTimeMillis();
        sender.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("configuration.reloaded"), end - start));
    }
}
