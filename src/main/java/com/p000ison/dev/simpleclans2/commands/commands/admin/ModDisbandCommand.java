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
 *     Created: 11.09.12 20:09
 */

package com.p000ison.dev.simpleclans2.commands.commands.admin;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * Represents a ModDisbandCommand
 */
public class ModDisbandCommand extends GenericConsoleCommand {


    public ModDisbandCommand(SimpleClans plugin)
    {
        super("ModDisband", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.disband"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("disband.command"));
        setPermission("simpleclans.mod.disband");
    }

    @Override
    public String getMenu()
    {
        return ChatColor.DARK_RED + MessageFormat.format(Language.getTranslation("0.disband.1.disband.your.clan"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        Clan clan = plugin.getClanManager().getClan(args[0]);

        if (clan != null) {
            plugin.getAnnouncer().announce(MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
            clan.disband();
        } else {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clan.matched"));
        }
    }
}