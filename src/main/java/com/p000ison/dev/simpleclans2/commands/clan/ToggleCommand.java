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
 *     Last modified: 03.11.12 12:02
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a ToggleCommand
 */
public class ToggleCommand extends GenericPlayerCommand {

    public ToggleCommand(SimpleClans plugin)
    {
        super("Command", plugin);
        setArgumentRange(1, 2);
        setUsages(Language.getTranslation("usage.toggle", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("toggle.command"));
        setPermission("simpleclans.member.toggle.*");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
//        if (cp != null) {
//            StringBuilder toggles = new StringBuilder();
//
//            if (cp.isLeader() && plugin.getPermissionsManager().has(sender, "simpleclans.member.tag-toggle")) {
//                toggles.append("tag/");
//            }
//            if (cp.getClan().isVerified()) {
//                if (plugin.hasSpout() && plugin.getSettingsManager().isClanCapes() && plugin.getPermissionsManager().has(sender, "simpleclans.member.cape-toggle")) {
//                    toggles.append("cape/");
//                }
//                if (cp.isTrusted()) {
//                    if (plugin.getPermissionsManager().has(sender, "simpleclans.member.bb-toggle")) {
//                        toggles.append("bb/");
//                    }
//                    if (plugin.getPermissionsManager().has(sender, "simpleclans.member.tag-toggle")) {
//                        toggles.append("tag/");
//                    }
//                }
//            }
//            return toggles.length() == 0 ? null : MessageFormat.format(Language.getTranslation("0.toggle.command"), plugin.getSettingsManager().getCommandClan(), ChatColor.WHITE, Helper.stripTrailing(toggles.toString(), "/"));
//        }

        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        String cmd = args[0];

        if (cmd.equalsIgnoreCase("cape")) {
            if (player.hasPermission("simpleclans.member.cape-toggle")) {
                ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

                if (cp != null) {
                    Clan clan = cp.getClan();

                    if (clan.isVerified()) {
                        if (cp.isCapeEnabled()) {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("capeoff"));
                            cp.setCapeEnabled(false);
                            SpoutSupport spout = plugin.getSpoutSupport();
                            if (spout.isEnabled()) {
                                spout.clearCape(cp);
                            }
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("capeon"));
                            cp.setCapeEnabled(true);
                        }
                        clan.update();
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("insufficient.permissions"));
            }
        }

        if (cmd.equalsIgnoreCase("bb")) {
            if (player.hasPermission("simpleclans.member.bb-toggle")) {
                ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

                if (cp != null) {
                    Clan clan = cp.getClan();

                    if (clan.isVerified()) {
                        if (cp.isBBEnabled()) {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("bboff"));
                            cp.setBBEnabled(false);
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("bbon"));
                            cp.setBBEnabled(true);
                        }
                        clan.update();
                    }
                }
            }
        }

        if (cmd.equalsIgnoreCase("list")) {
            if (player.hasPermission("simpleclans.member.toggle.list")) {
                ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

                if (cp != null) {
                    Clan clan = cp.getClan();

                    if (clan.isVerified()) {
                        if (cp.isBBEnabled()) {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("bboff"));
                            cp.setBBEnabled(false);
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("bbon"));
                            cp.setBBEnabled(true);
                        }
                        clan.update();
                    }
                }
            }
        }

//        if (cmd.equalsIgnoreCase("tag")) {
//            if (plugin.getPermissionsManager().has(player, "simpleclans.member.tag-toggle")) {
//                ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
//
//                if (cp != null) {
//                    Clan clan = cp.getClan();
//
//                    if (clan.isVerified()) {
//                        if (cp.isTagEnabled()) {
//                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("tagoff"));
//                            cp.setTagEnabled(false);
//                        } else {
//                            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("tagon"));
//                            cp.setTagEnabled(true);
//                        }
//                        plugin.getStorageManager().updateClanPlayer(cp);
//                    }
//                }
//            }
//        }

//        if (cmd.equalsIgnoreCase("all-seeing-eye") || cmd.equalsIgnoreCase("ase")) {
//            if (plugin.getPermissionsManager().has(sender, "simpleclans.admin.all-seeing-eye-toggle")) {
//                ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
//
//                if (cp != null) {
//                    cp.setAllSeeingEyeEnabled(!cp.isAllSeeingEyeEnabled());
//                } else {
//                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("insufficient.permissions"));
//                }
//            }
//        }
    }
}