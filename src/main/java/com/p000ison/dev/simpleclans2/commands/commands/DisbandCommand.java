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
 *     Created: 11.09.12 19:46
 */

package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class DisbandCommand extends GenericPlayerCommand {


    public DisbandCommand(SimpleClans plugin)
    {
        super("Disband", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.disband"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("disband.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.isLeader()) {
                return ChatColor.DARK_RED + MessageFormat.format(Language.getTranslation("menu.disband"), plugin.getSettingsManager().getClanCommand());
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

            if (!clan.isLeader(cp)) {
                player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                return;
            }

            if (clan.getLeaders().size() == 1) {
                clan.announce(cp, MessageFormat.format(Language.getTranslation("clan.has.been.disbanded"), clan.getName()));
                clan.disband();
            } else {
//              todo-requests
//                plugin.getRequestManager().addDisbandRequest(cp, clan);
                player.sendMessage(ChatColor.AQUA + Language.getTranslation("clan.disband.vote.has.been.requested.from.all.leaders"));
            }

        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}
