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

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.Command;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Max
 */
public class HelpCommand extends GenericConsoleCommand {

    private static final int CMDS_PER_PAGE = 12;


    public HelpCommand(SimpleClans plugin)
    {
        super("Help", plugin);
        setUsages(String.format("/%s help §8[page#]", plugin.getSettingsManager().getClanCommand()));
        setArgumentRange(0, 1);
        setIdentifiers("sc", "simpleclans", plugin.getSettingsManager().getClanCommand(), "help");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format("/{0} help §8[page#]", plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        int page = 0;

        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(Language.getTranslation("number.format"));
                return;
            }
        }

        Set<Command> sortCommands = plugin.getCommandManager().getCommands();
        List<Command> commands = new ArrayList<Command>();

        ClanPlayer cp = null;

        if (sender instanceof Player) {
            cp = plugin.getClanPlayerManager().getClanPlayer(sender.getName());
        }

        // Build list of permitted commands
        for (Command command : sortCommands) {
            if (!command.hasPermission(sender)) {
                continue;
            }

            if (command instanceof GenericConsoleCommand) {
                if (((GenericConsoleCommand) command).getMenu() != null) {
                    commands.add(command);
                }
            } else {
                if (((GenericPlayerCommand) command).getMenu(cp) != null) {
                    commands.add(command);
                }
            }
        }

        int numPages = commands.size() / CMDS_PER_PAGE;
        if (commands.size() % CMDS_PER_PAGE != 0) {
            numPages++;
        }

        if (page >= numPages || page < 0) {
            page = 0;
        }
        sender.sendMessage(plugin.getSettingsManager().getServerName() + " <" + (page + 1) + "/" + numPages + "> §7" + Language.getTranslation("clan.commands"));

        int start = page * CMDS_PER_PAGE;
        int end = start + CMDS_PER_PAGE;
        if (end > commands.size()) {
            end = commands.size();
        }
        StringBuilder menu = new StringBuilder();
        for (int c = start; c < end; c++) {
            Command cmd = commands.get(c);

            String commandMenu;

            if (cmd instanceof GenericConsoleCommand) {
                commandMenu = ((GenericConsoleCommand) cmd).getMenu();
            } else {
                commandMenu = ((GenericPlayerCommand) cmd).getMenu(cp);
            }

            menu.append("   ").append(commandMenu).append("\n").append(ChatColor.RESET);


//            String[] commandMenu = cmd.getMenu();
//            if (commandMenu != null) {
//                for (String usage : commandMenu) {
//                    menu.append("   §b").append(usage).append("\n");
//                }
//            }
        }
        sender.sendMessage(menu.toString());
    }
}
