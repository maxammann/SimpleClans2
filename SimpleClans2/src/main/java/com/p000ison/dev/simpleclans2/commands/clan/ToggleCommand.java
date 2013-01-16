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
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a ToggleCommand
 */
public class ToggleCommand extends GenericPlayerCommand {

    public ToggleCommand(SimpleClans plugin) {
        super("Command", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.toggle", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("toggle.command"));
        setPermission("simpleclans.member.toggle.cape", "simpleclans.member.toggle.bb");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null) {
            return Language.getTranslation("menu.toggle", plugin.getSettingsManager().getClanCommand());
        }

        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        String action = args[0];

        if (action.equalsIgnoreCase("cape")) {
            if (player.hasPermission("simpleclans.member.toggle.cape")) {
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
        } else if (action.equalsIgnoreCase("bb")) {
            if (player.hasPermission("simpleclans.member.toggle.bb")) {
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
    }
}