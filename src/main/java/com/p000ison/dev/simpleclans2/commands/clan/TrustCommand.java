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
 *     Last modified: 13.10.12 14:07
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a TrustCommand
 */
public class TrustCommand extends GenericPlayerCommand {

    public TrustCommand(SimpleClans plugin)
    {
        super("Trust", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation(("usage.trust"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("trust.command"));
        setPermission("simpleclans.leader.settrust");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null && cp.isLeader() && cp.getClan().isVerified()) {
            return Language.getTranslation("menu.trust", plugin.getSettingsManager().getClanCommand());
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
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                return;
            }
            ClanPlayer trusted = plugin.getClanPlayerManager().getClanPlayer(args[0]);

            if (trusted == null) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.player.matched"));
                return;
            }

            if (trusted.equals(cp)) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.cannot.trust.yourself"));
                return;
            }

            if (!clan.isMember(trusted)) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.is.not.a.member.of.your.clan"));
                return;
            }

            if (clan.isLeader(trusted)) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("leaders.are.already.trusted"));
            }

            if (!trusted.isTrusted()) {
                clan.addBBMessage(cp, Language.getTranslation("has.been.given.trusted.status.by", trusted.getName(), player.getName()));
                trusted.setTrusted(true);
                trusted.update();
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("this.player.is.already.trusted"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}