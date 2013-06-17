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

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.commandlib.Command;
import com.p000ison.dev.commandlib.CommandExecutor;
import com.p000ison.dev.commandlib.CommandHandler;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Represents a WarAdminCommand
 */
public class WarAdminCommand extends GenericConsoleCommand {

    public WarAdminCommand(SimpleClans plugin) {
        super("War (Admin)", plugin);
        setDescription(Language.getTranslation("description.war.admin"));
        setIdentifiers(Language.getTranslation("war.admin.command"));

        CommandExecutor executor = plugin.getCommandManager();

        Command cancel = executor.buildByMethod(this, "cancel")
                .setDescription(Language.getTranslation("description.war.admin.cancel"))
                .setIdentifiers(Language.getTranslation("cancel.command"))
                .addPermission("simpleclans.admin.warcontrol")

                .addArgument(Language.getTranslation("argument.tag"))
                .addArgument(Language.getTranslation("argument.tag"));

        this.addSubCommand(cancel);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
    }

    @Override
    public boolean allowExecution(com.p000ison.dev.commandlib.CommandSender sender) {
        return false;
    }

    @CommandHandler(name = "Cancel war")
    public void cancel(com.p000ison.dev.commandlib.CommandSender sender, CallInformation info) {
        String[] arguments = info.getArguments();
        Clan clan1 = getPlugin().getClanManager().getClan(arguments[0]);
        Clan clan2 = getPlugin().getClanManager().getClan(arguments[1]);

        if (clan1 == null || clan2 == null) {
            sender.sendMessage(ChatColor.DARK_RED + Language.getTranslation("no.clan.matched"));
            return;
        }

        boolean clan1War = clan1.isWarring(clan2);
        boolean clan2War = clan2.isWarring(clan1);

        if (clan1War || clan2War) {
            sender.sendMessage(ChatColor.AQUA + Language.getTranslation("you.are.no.longer.at.war", clan1.getTag(), clan2.getTag()));

            if (clan1War) {
                clan1.removeWarringClan(clan2);
            }

            if (clan2War) {
                clan2.removeWarringClan(clan1);
            }

        } else {
            sender.sendMessage(ChatColor.DARK_RED + Language.getTranslation("clans.not.at.war"));
        }
    }
}