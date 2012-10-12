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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Random;
import java.util.Set;

/**
 * @author phaed
 */
public class HomeRegroupCommand extends GenericPlayerCommand {

    private Random random = new Random();

    public HomeRegroupCommand(SimpleClans plugin)
    {
        super("HomeRegroup", plugin);
        setArgumentRange(0, 2);
        setUsages(MessageFormat.format(Language.getTranslation("usage.home.regroup"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("home.regroup.command"));
        setPermission("simpleclans.leader.regroup");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("menu.home.regroup"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String command, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (cp.isTrusted()) {

                    Location loc = player.getLocation();

                    if (cp.isLeader()) {

                        Set<ClanPlayer> members = clan.getAllMembers();
                        System.out.println(members);

                        for (ClanPlayer clanPlayer : members) {
                            Player iPlayer = clanPlayer.toPlayer();

                            if (iPlayer == null || iPlayer.equals(player)) {
                                continue;
                            }

                            int x = loc.getBlockX();
                            int z = loc.getBlockZ();

                            int xx = random.nextInt(2) - 1;
                            int zz = random.nextInt(2) - 1;

                            if (xx == 0 && zz == 0) {
                                xx = 1;
                            }

                            x = x + xx;
                            z = z + zz;

                            iPlayer.teleport(new Location(loc.getWorld(), x + .5, loc.getBlockY(), z + .5));
                        }

                        player.sendMessage(MessageFormat.format(ChatColor.AQUA + Language.getTranslation("clan.regrouped"), ChatColor.YELLOW + GeneralHelper.locationToString(loc)));

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
