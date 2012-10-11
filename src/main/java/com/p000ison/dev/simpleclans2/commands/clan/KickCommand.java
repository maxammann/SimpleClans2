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


public class KickCommand extends GenericPlayerCommand {

    public KickCommand(SimpleClans plugin)
    {
        super("Kick", plugin);
        setArgumentRange(1, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.kick"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("kick.command"));
        setPermission("simpleclans.leader.kick");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.isLeader()) {
                return MessageFormat.format(Language.getTranslation("menu.kick"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isLeader(cp)) {
                ClanPlayer kicked = plugin.getClanPlayerManager().getAnyClanPlayer(args[0]);

                if (kicked == null) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("no.player.matched"));
                    return;
                }

                if (kicked.getName().equals(player.getName())) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("you.cannot.kick.yourself"));
                    return;
                }

                if (!clan.isMember(kicked)) {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("the.player.is.not.a.member.of.your.clan"));
                    return;
                }

                if (!clan.isLeader(kicked)) {
                    clan.addBBMessage(cp, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("has.been.kicked.by"), kicked, player.getName()));
                    clan.removeMember(kicked);
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("you.cannot.kick.another.leader"));
                }

            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            }
        } else {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }

    }
}
