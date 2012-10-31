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

package com.p000ison.dev.simpleclans2.commands.clan.bb;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a BBCommand
 */
public class BBAddCommand extends GenericPlayerCommand {


    public BBAddCommand(SimpleClans plugin)
    {
        super("BBAdd", plugin);
        setArgumentRange(0, 500);
        setUsages(MessageFormat.format(Language.getTranslation("usage.bb.add"), plugin.getSettingsManager().getBBCommand()));
        setIdentifiers(Language.getTranslation("bb.add.command"));
        setPermission("simpleclans.member.bb-add");
        setType(Type.BB);
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.getClan().isVerified()) {
            return MessageFormat.format(Language.getTranslation("menu.bb.add"), plugin.getSettingsManager().getBBCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isVerified()) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));

            return;
        }

        if (cp.isTrusted()) {
            String msg = GeneralHelper.arrayToString(args);
            if (msg == null) {
                player.sendMessage(ChatColor.DARK_RED + Language.getTranslation("please.enter.message"));
                return;
            }
            clan.addBBMessage(cp, msg);
            player.sendMessage(ChatColor.AQUA + Language.getTranslation("bb.added"));
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
        }
    }
}
