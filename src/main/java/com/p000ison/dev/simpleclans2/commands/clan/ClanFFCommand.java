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

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ClanFFCommand
 */
public class ClanFFCommand extends GenericPlayerCommand {


    public ClanFFCommand(SimpleClans plugin)
    {
        super("Clanff", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.clanff"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("clanff.command"));
        setPermission("simpleclans.leader.ff");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if ((cp.isLeader() || cp.hasRankPermission("manage.clanff"))) {
                return MessageFormat.format(Language.getTranslation("menu.clanff"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {

        if (player.hasPermission("simpleclans.leader.ff")) {
            ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isLeader(cp) || cp.hasRankPermission("manage.clanff")) {
                    if (args.length == 1) {
                        String action = args[0];

                        if (action.equalsIgnoreCase(Language.getTranslation("allow"))) {
                            clan.addBBMessage(cp, Language.getTranslation("clan.wide.friendly.fire.is.allowed"));
                            clan.setFriendlyFire(true);
                            clan.update();
                        } else if (action.equalsIgnoreCase(Language.getTranslation("block"))) {
                            clan.addBBMessage(cp, Language.getTranslation("clan.wide.friendly.fire.blocked"));
                            clan.setFriendlyFire(false);
                            clan.update();
                        } else {
                            player.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("usage.clanff"), plugin.getSettingsManager().getClanCommand()));
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }
}