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
 *     Created: 11.09.12 21:02
 */

package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class HomeSetCommand extends GenericPlayerCommand {


    public HomeSetCommand(SimpleClans plugin)
    {
        super("SetHome", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.home.set"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("home.set.command"));
        setPermission("simpleclans.leader.home-set");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("home.set.menu"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (cp.isTrusted()) {

                    Location loc = player.getLocation();

                    if (cp.isLeader()) {
                        if (plugin.getPreciousStonesSupport().isTeleportAllowed(player, loc)) {
                            if (plugin.getSettingsManager().isSetHomeOnlyOnce() && clan.getFlags().getHomeLocation() != null && !player.hasPermission("simpleclans.mod.home")) {
                                player.sendMessage(ChatColor.RED + Language.getTranslation("home.base.only.once"));
                                return;
                            }

                            clan.getFlags().setHomeLocation(loc);
                            player.sendMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("hombase.set"), ChatColor.YELLOW + GeneralHelper.locationToString(loc)));
                        } else {
                            player.sendMessage(ChatColor.RED + Language.getTranslation("no.teleport"));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("only.trusted.players.can.access.clan.vitals"));
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
