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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.Announcer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class DisbandCommand extends GenericConsoleCommand {

    public DisbandCommand(SimpleClans plugin)
    {
        super("Disband", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.disband"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("disband.command"));
        setPermission("simpleclans.mod.disband");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.disband"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Clan clan = plugin.getClanManager().getClan(args[0]);

        if (clan != null) {
            Announcer.announce(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
            clan.disband();
        } else {
            sender.sendMessage(Language.getTranslation("no.clan.matched"));
        }
    }
}
