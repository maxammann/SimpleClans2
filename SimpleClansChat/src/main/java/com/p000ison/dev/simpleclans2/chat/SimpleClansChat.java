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
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat;

import com.dthielke.herochat.Herochat;
import com.p000ison.dev.simpleclans2.Flags;
import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.chat.channels.Channel;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCDepreciatedChatEvent;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCHeroChatListener;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCPlayerListener;
import com.p000ison.dev.simpleclans2.chat.util.Logging;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import net.krinsoft.chat.ChatCore;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class SimpleClansChat extends JavaPlugin {
    private SCCore core;
    private SettingsManager settingsManager;
    private static Permission permissions = null;
    private static Chat chat = null;
    private ChatCore chatSuite;

    @Override
    public void onEnable()
    {
        Logging.setInstance(getLogger());

        if (!hookSimpleClans()) {
            Logging.debug(Level.SEVERE, ChatColor.RED + "SimpleClans was not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settingsManager = new SettingsManager(this);

        chatSuite = setupChatSuite();

        PluginManager pluginManager = getServer().getPluginManager();

        if (setupHeroChat()) {
            pluginManager.registerEvents(new SCCHeroChatListener(this), this);
            Logging.debug("Hooked Herochat!");
        } else {
            SCCPlayerListener listener = new SCCPlayerListener(this);
            if (settingsManager.isDepreciationMode()) {
                pluginManager.registerEvents(new SCCDepreciatedChatEvent(listener), this);
            } else {
                pluginManager.registerEvents(listener, this);
            }

        }

        setupPermissions();
        setupChat();
    }

    @Override
    public void onDisable()
    {
        permissions = null;
        chat = null;
        Logging.close();
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = (Permission) rsp.getProvider();
        return permissions != null;
    }

    public boolean setupHeroChat()
    {
        try {
            for (Plugin plugin : this.getServer().getPluginManager().getPlugins()) {
                if (plugin instanceof Herochat) {
                    return true;
                }
            }
        } catch (NoClassDefFoundError ignored) {
        }
        return false;
    }

    private ChatCore setupChatSuite()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("ChatSuite");

        return (ChatCore) plugin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ClanPlayer cp = this.getClanPlayerManager().getClanPlayer(player);
            Flags flags = this.getClanPlayerManager().getClanPlayer(player).getFlags();
            String cmd = command.getName();
            if (cmd.equalsIgnoreCase(".")) {
                handleChannelCommand(Channel.CLAN, flags, args, player);
            } else if (cmd.equalsIgnoreCase("ally")) {
                handleChannelCommand(Channel.ALLY, flags, args, player);
            } else if (cmd.equalsIgnoreCase("global")) {
                if (args[0].equalsIgnoreCase("on")) {
                    Set set = flags.getSet("disabledChannels");
                    if (set == null) {
                        set = new HashSet();
                        flags.set("disabledChannels", set);
                    }
                    set.add(Channel.GLOBAL.getId());
                    sender.sendMessage("You turned the global chat on!");
                } else if (args[0].equalsIgnoreCase("off")) {
                    Set set = flags.getSet("disabledChannels");
                    if (set == null) {
                        set = new HashSet();
                        flags.set("disabledChannels", set);
                    }
                    set.remove(Channel.GLOBAL.getId());
                    sender.sendMessage("You turned the global chat off!");
                } else {
                    player.sendMessage("Command not found!");
                }
            }

            cp.update();

            System.out.println(flags.getData());
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void handleChannelCommand(Channel channel, Flags flags, String[] args, CommandSender player)
    {
        if (args[0].equalsIgnoreCase("join")) {
            flags.set("channel", channel.getId());
            player.sendMessage("You entered the " + channel.name().toLowerCase() + " channel! All messages go now there!");
        } else if (args[0].equalsIgnoreCase("leave")) {
            flags.removeEntry("channel");
            player.sendMessage("You left the " + channel.name().toLowerCase() + " channel!");
        } else if (args[0].equalsIgnoreCase("on")) {
            Set set = flags.getSet("disabledChannels");
            if (set == null) {
                set = new HashSet();
                flags.set("disabledChannels", set);
            }
            set.add(channel.getId());
            player.sendMessage("You turned the global chat on!");
        } else if (args[0].equalsIgnoreCase("off")) {
            Set set = flags.getSet("disabledChannels");
            if (set == null) {
                set = new HashSet();
                flags.set("disabledChannels", set);
            }
            set.remove(channel.getId());
            player.sendMessage("You turned the global chat off!");
        } else {
            player.sendMessage("Command not found!");
        }
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = (Chat) rsp.getProvider();
        return chat != null;
    }

    private boolean hookSimpleClans()
    {
        try {
            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
                if (plugin instanceof SCCore) {
                    this.core = (SCCore) plugin;
                    return true;
                }
            }
        } catch (NoClassDefFoundError e) {
            return false;
        }

        return false;
    }

    public static String getGroup(Player player)
    {
        if (permissions == null) {
            Logging.debug(Level.SEVERE, "Failed to get group! No permissions plugin found!");
            return null;
        }

        try {
            return permissions.getPrimaryGroup(player);
        } catch (Exception e) {
            Logging.debug(e, "Failed to get group! (Problem with Vault!)");
        }
        return null;
    }

    public static String getPrefix(Player player)
    {
        if (chat == null) {
            Logging.debug(Level.SEVERE, "Failed to get prefix! No chat plugin found!");
            return null;
        }

        try {
            return chat.getPlayerPrefix(player);
        } catch (Exception e) {
            Logging.debug(Level.SEVERE, "Failed to get prefix! (Problem with Vault!)");
        }
        return null;
    }

    public ClanPlayerManager getClanPlayerManager()
    {
        return this.core.getClanPlayerManager();
    }

    public SettingsManager getSettingsManager()
    {
        return this.settingsManager;
    }

    public ChatCore getChatSuite()
    {
        return chatSuite;
    }

    public String formatComplete(String format, Player player, String message)
    {
        ClanPlayer clanPlayer = this.getClanPlayerManager().getClanPlayer(player);

        String clanTag = null;
        String rankTag = null;

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();
            Rank rank = clanPlayer.getRank();

            if (rank != null) {
                rankTag = rank.getTag();
            }

            if (clan != null) {
                clanTag = clan.getTag();
            }
        }

        final String playerVariable = "-player";
        final String clanVariable = "-clan";
        final String rankVariable = "-rank";
        final String groupVariable = "-group";
        final String prefixVariable = "-prefix";
        final String messageVariable = "-message";


        if (format.contains(playerVariable)) {
            format = format.replace(playerVariable, player.getDisplayName());
        }

        if (format.contains(clanVariable)) {
            format = format.replace(clanVariable, clanTag == null ? "" : formatVariable(clanVariable, clanTag));
        }

        if (format.contains(rankVariable)) {
            format = format.replace(rankVariable, rankTag == null ? "" : formatVariable(rankVariable, rankTag));
        }

        if (format.contains(groupVariable)) {
            final String group = SimpleClansChat.getGroup(player);
            format = format.replace(groupVariable, group == null ? "" : formatVariable(groupVariable, group));
        }

        if (format.contains(prefixVariable)) {
            final String prefix = SimpleClansChat.getPrefix(player);
            format = format.replace(prefixVariable, prefix == null ? "" : formatVariable(prefixVariable, prefix));
        }

        if (format.contains(messageVariable)) {
            format = format.replace(messageVariable, message);
        }

        return format;
    }

    public String formatVariable(String variable, String input)
    {
        SettingsManager settings = this.getSettingsManager();
        return settings.getVariable(variable).format(input);
    }

    public String formatCompatibility(String format, String playerName)
    {
        ClanPlayer clanPlayer = this.getClanPlayerManager().getClanPlayerExact(playerName);

        String clanTag = null;
        String rankTag = null;

        if (clanPlayer != null) {
            Clan clan = clanPlayer.getClan();

            if (clan != null) {
                clanTag = clan.getTag();
                Rank rank = clanPlayer.getRank();
                if (rank != null) {
                    rankTag = rank.getTag();
                }
            }
        }

        final String clanVariable = "-clan";
        final String rankVariable = "-rank";
        ChatColor lastColor;

        if (format.contains(clanVariable)) {
            lastColor = ChatBlock.getLastColors(format, format.indexOf(clanVariable), '§', '&');
            if (lastColor != null && clanTag != null) {
                clanTag += lastColor.toString();
            }

            String variable = formatVariable(clanVariable, clanTag);
            format = format.replace(clanVariable, clanTag == null ? "" : variable);
        }

        if (format.contains(rankVariable)) {
            lastColor = ChatBlock.getLastColors(format, format.indexOf(rankVariable), '§', '&');

            if (lastColor != null && rankTag != null) {
                rankTag += lastColor.toString();
            }

            String variable = formatVariable(rankVariable, rankTag);
            format = format.replace(rankVariable, rankTag == null ? "" : variable);
        }

        return format;
    }
}