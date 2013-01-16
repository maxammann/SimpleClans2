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
 *     Last modified: 1/9/13 9:44 PM
 */


package com.p000ison.dev.simpleclans2.commands;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.command.Command;
import com.p000ison.dev.simpleclans2.api.command.CommandManager;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents a CraftCommandManager
 */
public class CraftCommandManager implements CommandManager {

    private List<Command> commands;
    private final SimpleClans plugin;

    public CraftCommandManager(SimpleClans plugin) {
        this.plugin = plugin;
        commands = new ArrayList<Command>();
    }

    @Override
    public void addCommand(Command command) {
        commands.add(command);
    }

    @Override
    public void insertCommand(int position, Command command) {
        commands.add(position, command);
    }

    @Override
    public void removeCommand(Command command) {
        commands.remove(command);
    }

    @Override
    public Command getCommand(String name) {
        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)) {
                return cmd;
            }
        }

        return null;
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    public synchronized void execute(CommandSender sender, String command, Command.Type cmdType, String[] args) {
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
                displayHelp(sender, cmdType, 0);
                return;
            } else if (args.length > 0) {
                if (args[0].equals("help")) {
                    if (args.length == 2) {
                        displayHelp(sender, cmdType, getPage(args[1]));
                        return;
                    } else {
                        displayHelp(sender, cmdType, 0);
                        return;
                    }
                } else if (args.length == 1) {
                    try {
                        int page = Integer.parseInt(args[0]);
                        displayHelp(sender, cmdType, page - 1);
                        return;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            Command helpCommand = null;
            boolean noPermission = false;

            for (Command cmd : commands) {

                Command.Type type = cmd.getType();

                if (type != null && cmdType != cmd.getType()) {
                    continue;
                }

                if (cmd.isIdentifier(identifier)) {

                    if (!cmd.hasPermission(sender)) {
                        ChatBlock.sendMessage(sender, ChatColor.DARK_RED + Language.getTranslation("insufficient.permissions"));
                        noPermission = true;
                        continue;
                    } else if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                        helpCommand = cmd;
                        continue;
                    } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        displayCommandHelp(cmd, sender);
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


            if (!noPermission) {
                ChatBlock.sendMessage(sender, ChatColor.DARK_RED + "Command not found!");
            }
        } catch (RuntimeException e) {
            Logging.debug(e, "Failed at running a SimpleClans command!", true);
        }
    }

    private void displayCommandHelp(Command cmd, CommandSender sender) {
        ChatBlock.sendMessage(sender, "§cCommand:§e " + cmd.getName());
        String[] usages = cmd.getUsages();
        StringBuilder sb = new StringBuilder("§cUsage:§e ").append(usages[0]).append("\n");

        for (int i = 1; i < usages.length; i++) {
            sb.append("           ").append(usages[i]).append("\n");
        }

        ChatBlock.sendMessage(sender, sb.toString());
    }

    private void displayHelp(CommandSender sender, Command.Type commandType, int page) {
        List<Command> sortCommands = plugin.getCommandManager().getCommands();
        List<String> commands = new ArrayList<String>();

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
                String menu = ((GenericConsoleCommand) cmd).getMenu();
                if (((GenericConsoleCommand) cmd).getMenu() != null) {
                    commands.add(menu);
                }
            } else {
                String menu = ((GenericPlayerCommand) cmd).getMenu(cp);
                if (menu != null) {
                    commands.add(menu);
                }
            }
        }


        int size = commands.size();

        if (size == 0) {
            ChatBlock.sendMessage(sender, ChatColor.DARK_RED + "No commands found!");
            return;
        }

        int[] boundings = getBoundings(size, page);

        ChatBlock.sendHead(sender, plugin.getSettingsManager().getServerName() + " <" + (boundings[3] + 1) + "/" + boundings[2] + ">", Language.getTranslation("clan.commands"));

        StringBuilder menu = new StringBuilder("\n");


        for (int c = boundings[0]; c < boundings[1]; c++) {
            String cmdMenu = commands.get(c);

            menu.append(MessageFormat.format(plugin.getSettingsManager().getHelpFormat(), cmdMenu)).append(ChatColor.RESET);

            if (c != boundings[1]) {
                menu.append('\n');
            }
        }


        ChatBlock.sendMessage(sender, menu.toString());
        ChatBlock.sendBlank(sender, 2);
    }

    public int[] getBoundings(int completeSize, int page) {
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

    public static int getPage(String[] args) {
        if (args.length == 1) {
            return getPage(args[0]);
        }
        return 0;
    }

    public static int getPage(String pageString) {
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
