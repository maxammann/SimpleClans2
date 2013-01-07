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
 *     Last modified: 04.11.12 17:04
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a WarAdminCommand
 */
public class WarAdminCommand extends GenericConsoleCommand {

    public WarAdminCommand(SimpleClans plugin)
    {
        super("WarAdmin", plugin);
        setArgumentRange(3, 3);
        setUsages(Language.getTranslation("usage.war.admin", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("war.admin.command"));
        setPermission("simpleclans.admin.warcontrol");
    }

    @Override
    public String getMenu()
    {
        if (plugin.isUpdate()) {
            return Language.getTranslation("menu.war.admin", plugin.getSettingsManager().getClanCommand());
        }

        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        Clan clan1 = plugin.getClanManager().getClan(args[1]);
        Clan clan2 = plugin.getClanManager().getClan(args[2]);

        if (clan1 == null || clan2 == null) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        if (args[0].equalsIgnoreCase("cancel")) {

            boolean clan1War = clan1.isWarring(clan2);
            boolean clan2War = clan2.isWarring(clan1);

            if (clan1War || clan2War) {
                ChatBlock.sendMessage(sender, ChatColor.AQUA + Language.getTranslation("you.are.no.longer.at.war", clan1.getTag(), clan2.getTag()));

                if (clan1War) {
                    clan1.removeWarringClan(clan2);
                }

                if (clan2War) {
                    clan2.removeWarringClan(clan1);
                }

            } else {
                ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("clans.not.at.war"));
            }
        }
    }
}