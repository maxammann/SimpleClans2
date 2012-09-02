/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */


package com.p000ison.dev.simpleclans2.commands;

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

    public boolean executeAll(final Player player, final CommandSender sender, String command, String label, String[] args)
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
                        displayCommandHelp(cmd, sender, player);
                        return true;
                    } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        displayCommandHelp(cmd, sender, player);
                        return true;
                    }


                    if (cmd instanceof GenericConsoleCommand) {
                        if (sender != null) {
                            ((GenericConsoleCommand) cmd).execute(sender, label, realArgs);
                        } else {
                            ((GenericConsoleCommand) cmd).execute((CommandSender) player, label, realArgs);
                        }
                    } else if (cmd instanceof GenericCommand) {
                        if (player != null) {
                            ((GenericPlayerCommand) cmd).execute(player, label, realArgs);
                        } else {
                            Logging.debug(Level.WARNING, "Failed at parsing the command :(");
                        }
                    } else {
                        Logging.debug(Level.WARNING, "Failed at parsing the command :(");
                    }

                    return true;
                }
            }
        }
        (sender == null ? player : sender).sendMessage(ChatColor.DARK_RED + "Command not found!");

        return true;
    }

    private void displayCommandHelp(Command cmd, CommandSender sender, Player player)
    {
        if (player == null) {
            sender.sendMessage("§cCommand:§e " + cmd.getName());
            String[] usages = cmd.getUsages();
            StringBuilder sb = new StringBuilder("§cUsage:§e ").append(usages[0]).append("\n");

            for (int i = 1; i < usages.length; i++) {
                sb.append("           ").append(usages[i]).append("\n");
            }
            sender.sendMessage(sb.toString());
        } else if (sender == null) {
            player.sendMessage("§cCommand:§e " + cmd.getName());
            String[] usages = cmd.getUsages();
            StringBuilder sb = new StringBuilder("§cUsage:§e ").append(usages[0]).append("\n");

            for (int i = 1; i < usages.length; i++) {
                sb.append("           ").append(usages[i]).append("\n");
            }
            player.sendMessage(sb.toString());
        }
    }

//    /**
//     * @param args
//     * @return
//     */
//    public void processClan(Player player, String[] args)
//    {
//        try {
//            if (plugin.getSettingsManager().isBanned(player.getName())) {
//                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
//                return;
//            }
//
//            String clanCommand = plugin.getSettingsManager().getCommandClan();
//
//            executeAll(player, null, clanCommand, clanCommand, args);
//
//        } catch (Exception ex) {
//            SimpleClans.debug(plugin.getLang("simpleclans.command.failure"), ex);
//        }
//    }
//
//    /**
//     * Process the accept command
//     *
//     * @param player
//     */
//    public void processAccept(Player player)
//    {
//        if (plugin.getSettingsManager().isBanned(player.getName())) {
//            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
//            return;
//        }
//
//        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
//
//        if (cp != null) {
//            Clan clan = cp.getClan();
//
//            if (clan.isLeader(player)) {
//                if (plugin.getRequestManager().hasRequest(clan.getTag())) {
//                    if (cp.getVote() == null) {
//                        plugin.getRequestManager().accept(cp);
//                        clan.leaderAnnounce(ChatColor.GREEN + MessageFormat.format(plugin.getLang("voted.to.accept"), Helper.capitalize(player.getName())));
//                    } else {
//                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.have.already.voted"));
//                    }
//                } else {
//                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
//                }
//            } else {
//                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
//            }
//        } else {
//            if (plugin.getRequestManager().hasRequest(player.getName().toLowerCase())) {
//                cp = plugin.getClanManager().getCreateClanPlayer(player.getName());
//                plugin.getRequestManager().accept(cp);
//            } else {
//                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
//            }
//        }
//    }
//
//    /**
//     * Process the deny command
//     *
//     * @param player
//     */
//    public void processDeny(Player player)
//    {
//        if (plugin.getSettingsManager().isBanned(player.getName())) {
//            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
//            return;
//        }
//
//        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
//
//        if (cp != null) {
//            Clan clan = cp.getClan();
//
//            if (clan.isLeader(player)) {
//                if (plugin.getRequestManager().hasRequest(clan.getTag())) {
//                    if (cp.getVote() == null) {
//                        plugin.getRequestManager().deny(cp);
//                        clan.leaderAnnounce(ChatColor.RED + MessageFormat.format(plugin.getLang("has.voted.to.deny"), Helper.capitalize(player.getName())));
//                    } else {
//                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.have.already.voted"));
//                    }
//                } else {
//                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.deny"));
//                }
//            } else {
//                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
//            }
//        } else {
//            if (plugin.getRequestManager().hasRequest(player.getName().toLowerCase())) {
//                cp = plugin.getClanManager().getCreateClanPlayer(player.getName());
//                plugin.getRequestManager().deny(cp);
//            } else {
//                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.deny"));
//            }
//        }
//    }
//
//    /**
//     * Process the more command
//     *
//     * @param player
//     */
//    public void processMore(Player player)
//    {
//        if (plugin.getSettingsManager().isBanned(player.getName())) {
//            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
//            return;
//        }
//
//        ChatBlock chatBlock = plugin.getStorageManager().getChatBlock(player);
//
//        if (chatBlock != null && chatBlock.size() > 0) {
//            chatBlock.sendBlock(player, plugin.getSettingsManager().getPageSize());
//
//            if (chatBlock.size() > 0) {
//                ChatBlock.sendBlank(player);
//                ChatBlock.sendMessage(player, plugin.getSettingsManager().getPageHeadingsColor() + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
//            }
//            ChatBlock.sendBlank(player);
//        } else {
//            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.more.to.see"));
//        }
//    }
}
