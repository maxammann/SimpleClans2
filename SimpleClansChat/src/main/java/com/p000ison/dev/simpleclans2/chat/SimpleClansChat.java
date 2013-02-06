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
import com.p000ison.dev.simpleclans2.api.Flags;
import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.api.rank.Rank;
import com.p000ison.dev.simpleclans2.chat.commands.AllyChannelCommand;
import com.p000ison.dev.simpleclans2.chat.commands.ClanChannelCommand;
import com.p000ison.dev.simpleclans2.chat.commands.GlobalChannelCommand;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCDepreciatedChatEvent;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCHeroChatListener;
import com.p000ison.dev.simpleclans2.chat.listeners.SCCPlayerListener;
import com.p000ison.dev.simpleclans2.chat.util.Logging;
import net.krinsoft.chat.ChatCore;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class SimpleClansChat extends JavaPlugin {
    private SCCore core;
    private SettingsManager settingsManager;
    private static Permission permissions = null;
    private static Chat chat = null;
    private ChatCore chatSuite;

    @Override
    public void onEnable() {
        Logging.setInstance(getLogger());
        SCChatLanguage.setInstance(new File(getDataFolder(), "languages"), Charset.defaultCharset());

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
            if (settingsManager.isDepreciationMode()) {
                pluginManager.registerEvents(new SCCDepreciatedChatEvent(this), this);
            } else {
                pluginManager.registerEvents(new SCCPlayerListener(this), this);
            }

        }

        core.getCommandManager().addCommand(new GlobalChannelCommand("GlobalChannel", core));
        core.getCommandManager().addCommand(new ClanChannelCommand("ClanChannel", core));
        core.getCommandManager().addCommand(new AllyChannelCommand("AllyChannel", core));

        setupPermissions();
        setupChat();
    }

    @Override
    public void onDisable() {
        permissions = null;
        chat = null;
        Logging.close();
        SCChatLanguage.clear();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

    public boolean setupHeroChat() {
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

    private ChatCore setupChatSuite() {
        Plugin plugin = getServer().getPluginManager().getPlugin("ChatSuite");

        return (ChatCore) plugin;
    }

    public static void addDisabledChannel(ClanPlayer clanPlayer, Channel channel, boolean removeFromDisabled) {
        if (clanPlayer == null) {
            return;
        }

        Flags flags = clanPlayer.getFlags();

        if (flags == null) {
            return;
        }

        Set<Byte> set = flags.getSet("disabledChannels");

        if (set == null) {
            set = new HashSet<Byte>();
            flags.set("disabledChannels", set);
        }

        if (removeFromDisabled) {
            set.remove(channel.getId());
        } else {
            set.add(channel.getId());
        }
    }

    public static void setChannel(ClanPlayer clanPlayer, Channel channel) {
        if (clanPlayer == null) {
            return;
        }

        Flags flags = clanPlayer.getFlags();

        if (flags == null) {
            return;
        }

        if (channel == null) {
            flags.removeEntry("channel");
        } else {
            flags.set("channel", channel.getId());
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean hookSimpleClans() {
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

    public static String getGroup(Player player) {
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

    public static String getPrefix(Player player) {
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

    public static String getSuffix(Player player) {
        if (chat == null) {
            Logging.debug(Level.SEVERE, "Failed to get suffix! No chat plugin found!");
            return null;
        }

        try {
            return chat.getPlayerSuffix(player);
        } catch (Exception e) {
            Logging.debug(Level.SEVERE, "Failed to get suffix! (Problem with Vault!)");
        }
        return null;
    }

    public ClanPlayerManager getClanPlayerManager() {
        return this.core.getClanPlayerManager();
    }

    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    public ChatCore getChatSuite() {
        return chatSuite;
    }

    public String formatComplete(String format, Player player, ClanPlayer clanPlayer) {
        if (clanPlayer == null) {
            clanPlayer = this.getClanPlayerManager().getClanPlayer(player);
        }

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
        final String suffixVariable = "-suffix";
        final String messageVariable = "-message";


        if (format.contains(playerVariable)) {
            format = format.replace(playerVariable, "%1$s");
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

        if (format.contains(suffixVariable)) {
            final String suffix = SimpleClansChat.getSuffix(player);
            format = format.replace(suffixVariable, suffix == null ? "" : formatVariable(suffixVariable, suffix));
        }


        if (format.contains(messageVariable)) {
            format = format.replace(messageVariable, "%2$s");
        }

        return ChatBlock.parseColors(format);
    }

    public String removeRetrievers(Set<Player> retrievers, ClanPlayer cp, Player player) {
        if (cp == null || cp.getFlags() == null) {
            return null;
        }

        byte flag = cp.getFlags().getByte("channel");

        String format = null;

        if (flag != -1) {
            Iterator<Player> players;

            Channel channel = Channel.getById(flag);

            switch (channel) {
                case ALLY:
                    //format for allies
                    format = formatComplete(getSettingsManager().getAllyChannelFormat(), player, cp);

                    players = retrievers.iterator();
                    while (players.hasNext()) {
                        Player retriever = players.next();
                        ClanPlayer iCP = getClanPlayerManager().getClanPlayer(retriever);
                        boolean isAllSeeing = retriever.hasPermission("simpleclans.admin.all-seeing-eye");
                        if (isChannelDisabled(iCP, Channel.ALLY) && (isAllSeeing || !cp.getClan().isAlly(iCP.getClan()))) {
                            players.remove();
                        } else if (isChannelDisabled(retriever, Channel.GLOBAL)) {
                            players.remove();
                        }
                    }
                    break;
                case CLAN:
                    //format for clan

                    format = formatComplete(getSettingsManager().getClanChannelFormat(), player, cp);

                    players = retrievers.iterator();
                    while (players.hasNext()) {
                        Player retriever = players.next();
                        ClanPlayer iCP = getClanPlayerManager().getClanPlayer(retriever);
                        boolean isAllSeeing = retriever.hasPermission("simpleclans.admin.all-seeing-eye");
                        if (isChannelDisabled(iCP, Channel.CLAN) && (isAllSeeing || !cp.getClan().isAnyMember(iCP))) {
                            players.remove();
                        } else if (isChannelDisabled(retriever, Channel.GLOBAL)) {
                            players.remove();
                        }
                    }
                    break;
                case GLOBAL:
                    break;
            }
        }

        return format;
    }


    private boolean isChannelDisabled(Player player, Channel channel) {
        return isChannelDisabled(getClanPlayerManager().getClanPlayer(player), channel);
    }

    private boolean isChannelDisabled(ClanPlayer player, Channel channel) {
        if (player == null || player.getFlags() == null) {
            return false;
        }
        Set<Byte> disabled = player.getFlags().getSet("disabledChannels");
        return disabled != null && disabled.contains(channel.getId());
    }

    public String formatVariable(String variable, String input) {
        SettingsManager settings = this.getSettingsManager();
        return settings.getVariable(variable).format(input);
    }

    public String formatCompatibility(String format, String playerName) {
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