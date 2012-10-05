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
 *     Created: 11.09.12 19:46
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class GlobalFFCommand extends GenericConsoleCommand {


    public GlobalFFCommand(SimpleClans plugin)
    {
        super("GlobalFF", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.globalff"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("globalff.command"));
        setPermission("simpleclans.mod.globalff");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.globalff"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        String action = args[0];

        if (action.equalsIgnoreCase(Language.getTranslation("allow"))) {
            if (plugin.getSettingsManager().isGlobalFF()) {
                sender.sendMessage(Language.getTranslation("global.friendly.fire.is.already.being.allowed"));
            } else {
                plugin.getSettingsManager().setGlobalFF(true);
                sender.sendMessage(Language.getTranslation("global.friendly.fire.is.set.to.allowed"));
            }
        } else if (action.equalsIgnoreCase(Language.getTranslation("auto"))) {
            if (!plugin.getSettingsManager().isGlobalFF()) {
                sender.sendMessage(Language.getTranslation("global.friendy.fire.is.already.being.managed.by.each.clan"));
            } else {
                plugin.getSettingsManager().setGlobalFF(false);
                sender.sendMessage(Language.getTranslation("global.friendy.fire.is.now.managed.by.each.clan"));
            }
        } else {
            sender.sendMessage(MessageFormat.format(Language.getTranslation("usage.globalff"), plugin.getSettingsManager().getClanCommand()));
        }
    }
}
