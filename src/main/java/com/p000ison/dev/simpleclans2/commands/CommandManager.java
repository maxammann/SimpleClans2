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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.commands;

import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a CommandManager
 */
public class CommandManager {

    private Set<Command> commands;

    public CommandManager()
    {
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

            Command helpCommand = null;

            for (Command cmd : commands) {
                if (cmd.isIdentifier(identifier)) {

                    Command.Type type = cmd.getType();

                    if (type != null && !type.getCommand().equals(command)) {
                        displayCommandHelp(cmd, sender);
                        return;
                    }

                    if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                        helpCommand = cmd;
                        continue;
                    } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        displayCommandHelp(cmd, sender);
                        return;
                    }

                    if (!cmd.hasPermission(sender)) {
                        sender.sendMessage(ChatColor.DARK_RED + Language.getTranslation("insufficient.permissions"));
                        return;
                    }

                    if (cmd instanceof GenericConsoleCommand) {
                        ((GenericConsoleCommand) cmd).execute(sender, realArgs);
                        return;
                    } else if (cmd instanceof GenericPlayerCommand) {
                        if (sender instanceof Player) {
                            ((GenericPlayerCommand) cmd).execute((Player) sender, realArgs);
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
            Logging.debug(e, "Failed at running a SimpleClans command!");
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
}
