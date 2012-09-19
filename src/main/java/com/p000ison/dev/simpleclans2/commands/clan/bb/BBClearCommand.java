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
 *     Created: 10.09.12 15:03
 */

package com.p000ison.dev.simpleclans2.commands.clan.bb;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a BBCommand
 */
public class BBClearCommand extends GenericPlayerCommand {


    public BBClearCommand(SimpleClans plugin)
    {
        super("BBClear", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bb.clear"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("bb.clear.command"));
        setPermission("simpleclans.member.bb-clear");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("menu.bb.clear"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        if (cp.isTrusted() && (cp.isLeader() || cp.hasPermission("manage.bb"))) {
            cp.getClan().clearBB();
            clan.update();
            player.sendMessage(ChatColor.RED + Language.getTranslation("cleared.bb"));
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
        }
    }
}
