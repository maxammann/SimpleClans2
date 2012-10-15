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


package com.p000ison.dev.simpleclans2.commands;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents a CommandManager
 */
public class CommandManager {

    private Set<Command> commands;
    private final SimpleClans plugin;

    public CommandManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        commands = new HashSet<Command>();
    }

    public void addCommand(Command command)
    {
        commands.add(command);
    }

    public void removeCommand(Command command)
    {
        commands.remove(command);
    }

    public Command getCommand(String name)
    {
        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)) {
                return cmd;
            }
        }

        return null;
    }

    public Set<Command> getCommands()
    {
        return commands;
    }


    public void execute(CommandSender sender, String command, String[] args)
    {
        try {

            String identifier;
            String[] realArgs;

            //Build the args; if the args length is 0 then build if from the base command
            if (args.length == 0) {
                identifier = command;
                realArgs = new String[0];
            } else {
                identifier = args[0];
                realArgs = Arrays.copyOfRange(args, 1, args.length);
            }

            //Display default help
            if (args.length == 0) {
                displayHelp(sender, Command.Type.getByCommand(command), 0);
                return;
            } else if (args.length > 0 && args[0].equals("help")) {
                if (args.length == 2) {
                    displayHelp(sender, Command.Type.getByCommand(command), getPage(args[1]));
                    return;
                } else {
                    displayHelp(sender, Command.Type.getByCommand(command), 0);
                    return;
                }
            }

            Command helpCommand = null;

            for (Command cmd : commands) {
                if (cmd.isIdentifier(identifier)) {

                    Command.Type type = cmd.getType();

                    if (type != null && !type.equals(cmd.getType())) {
                        displayCommandHelp(cmd, sender);
                        return;
                    }

                    if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                        helpCommand = cmd;
                        continue;
                    } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        displayCommandHelp(cmd, sender);
                        return;
                    } else if (!cmd.hasPermission(sender)) {
                        sender.sendMessage(ChatColor.DARK_RED + Language.getTranslation("insufficient.permissions"));
                        return;
                    }

                    if (cmd instanceof GenericConsoleCommand) {
                        ((GenericConsoleCommand) cmd).execute(sender, command, realArgs);
                        return;
                    } else if (cmd instanceof GenericPlayerCommand) {
                        if (sender instanceof Player) {
                            ((GenericPlayerCommand) cmd).execute((Player) sender, command, realArgs);
                            return;
                        }
                    } else {
                        Logging.debug(Level.WARNING, "Failed at parsing the command :(");
                    }
                }
            }

            if (helpCommand != null) {
                displayCommandHelp(helpCommand, sender);
                return;
            }

        } catch (RuntimeException e) {
            Logging.debug(e, "Failed at running a SimpleClans command!", true);
            return;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Command not found!");
    }

    private void displayCommandHelp(Command cmd, CommandSender sender)
    {
        sender.sendMessage("§cCommand:§e " + cmd.getName());
        String[] usages = cmd.getUsages();
        StringBuilder sb = new StringBuilder("§cUsage:§e ").append(usages[0]).append("\n");

        for (int i = 1; i < usages.length; i++) {
            sb.append("           ").append(usages[i]).append("\n");
        }

        sender.sendMessage(sb.toString());
    }

    private void displayHelp(CommandSender sender, Command.Type commandType, int page)
    {
        Set<Command> sortCommands = plugin.getCommandManager().getCommands();
        List<Command> commands = new ArrayList<Command>();

        ClanPlayer cp = null;

        if (sender instanceof Player) {
            cp = plugin.getClanPlayerManager().getClanPlayer(sender.getName());
        }

        // Build list of permitted commands
        for (Command cmd : sortCommands) {
            if (!cmd.hasPermission(sender)) {
                continue;
            }

            if (cmd.getType() != null && cmd.getType() != commandType) {
                continue;
            }

            if (cmd instanceof GenericConsoleCommand) {
                if (((GenericConsoleCommand) cmd).getMenu() != null) {
                    commands.add(cmd);
                }
            } else {
                if (cp != null && ((GenericPlayerCommand) cmd).getMenu(cp) != null) {
                    commands.add(cmd);
                }
            }
        }


        int size = commands.size();

        int[] boundings = getBoundings(size, page);

        ChatBlock.sendHead(sender, plugin.getSettingsManager().getServerName() + " <" + (boundings[3] + 1) + "/" + boundings[2] + ">", Language.getTranslation("clan.commands"));

        StringBuilder menu = new StringBuilder("\n");


        for (int c = boundings[0]; c < boundings[1]; c++) {
            Command cmd = commands.get(c);

            String commandMenu;

            if (cmd instanceof GenericConsoleCommand) {
                commandMenu = ((GenericConsoleCommand) cmd).getMenu();
            } else if (cp != null) {
                commandMenu = ((GenericPlayerCommand) cmd).getMenu(cp);
            } else {
                continue;
            }

            menu.append(MessageFormat.format(plugin.getSettingsManager().getHelpFormat(), commandMenu)).append(ChatColor.RESET);

            if (c != boundings[1]) {
                menu.append('\n');
            }
        }


        sender.sendMessage(menu.toString());
        ChatBlock.sendBlank(sender, 2);
    }

    public int[] getBoundings(int completeSize, int page)
    {
        int numPages = completeSize / plugin.getSettingsManager().getElementsPerPage();

        if (completeSize % plugin.getSettingsManager().getElementsPerPage() != 0) {
            numPages++;
        }

        if (page >= numPages || page < 0) {
            page = 0;
        }


        int start = page * plugin.getSettingsManager().getElementsPerPage();
        int end = start + plugin.getSettingsManager().getElementsPerPage();


        if (end > completeSize) {
            end = completeSize;
        }

        return new int[]{start, end, numPages, page};
    }

    public static int getPage(String[] args)
    {
        if (args.length == 1) {
            return getPage(args[0]);
        }
        return 0;
    }

    public static int getPage(String pageString)
    {
        try {
            int page = Integer.parseInt(pageString) - 1;
            if (page < 0) {
                page = 0;
            }
            return page;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
