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

package com.p000ison.dev.simpleclans2.commands.clan.home;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class HomeSetCommand extends GenericPlayerCommand {

    public HomeSetCommand(SimpleClans plugin) {
        super("HomeSet", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.home.set"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("home.set.command"));
        setPermission("simpleclans.leader.home-set");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null && cp.getClan().isVerified()) {
            return MessageFormat.format(Language.getTranslation("menu.home.set"), plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (cp.isTrusted()) {

                    Location loc = player.getLocation();

                    if (cp.isLeader()) {
                        if (plugin.getPreciousStonesSupport().isTeleportAllowed(player, loc)) {
                            if (plugin.getSettingsManager().isSetHomeOnlyOnce() && clan.getFlags().getHomeLocation() != null && !player.hasPermission("simpleclans.mod.home")) {
                                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("home.base.only.once"));
                                return;
                            }

                            if (SimpleClans.hasEconomy() && plugin.getSettingsManager().isPurchaseSetTeleport() && !cp.withdraw(plugin.getSettingsManager().getPurchaseTeleportSetPrice())) {
                                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                                return;
                            }

                            clan.getFlags().setHomeLocation(loc);
                            clan.update();
                            ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("hombase.set"), ChatColor.YELLOW + GeneralHelper.locationToString(loc)));
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.teleport"));
                        }
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.vitals"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
