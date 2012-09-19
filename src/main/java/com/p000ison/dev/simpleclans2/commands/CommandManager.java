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

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a CommandManager
 */
public class CommandManager {

    private SimpleClans plugin;
    private LinkedHashMap<String, Command> commands;

    public CommandManager(SimpleClans plugin)
    {
        this.plugin = plugin;
        commands = new LinkedHashMap<String, Command>();
    }

    public void addCommand(Command command)
    {
        commands.put(command.getName().toLowerCase(), command);
    }

    public void removeCommand(String command)
    {
        commands.remove(command);
    }

    public Command getCommand(String name)
    {
        return commands.get(name.toLowerCase());
    }

    public Set<Command> getCommands()
    {
        return new HashSet<Command>(commands.values());
    }


    public void execute(CommandSender sender, String command, String[] args)
    {
        String[] arguments;

        //Build the args; if the args length is 0 then build if from the base command
        if (args.length == 0) {
            arguments = new String[]{command};
        } else {
            arguments = args;
        }

        //Iterate through all arguments from the last to the first argument
        for (int argsIncluded = arguments.length; argsIncluded >= 0; argsIncluded--) {
            String identifier = "";
            //Build the identifier string
            for (int i = 0; i < argsIncluded; i++) {
                identifier += " " + arguments[i];
            }

            //trim the last ' '
            identifier = identifier.trim();
            for (Command cmd : commands.values()) {
                if (cmd.isIdentifier(identifier)) {
                    String[] realArgs = Arrays.copyOfRange(arguments, argsIncluded, arguments.length);


                    if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                        displayCommandHelp(cmd, sender);
                        return;
                    } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        displayCommandHelp(cmd, sender);
                        return;
                    }

                    if (!cmd.hasPermission(sender)) {
                        sender.sendMessage(ChatColor.DARK_RED + Language.getTranslation("insufficient.permissions"));
                        return;
                    }

                    if (cmd instanceof GenericConsoleCommand) {
                        ((GenericConsoleCommand) cmd).execute(sender, args);
                    } else if (cmd instanceof GenericPlayerCommand) {
                        if (sender instanceof Player) {
                            ((GenericPlayerCommand) cmd).execute((Player) sender, args);
                        }
                    } else {
                        Logging.debug(Level.WARNING, "Failed at parsing the command :(");
                    }
                }
            }
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
