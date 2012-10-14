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
*     Last modified: 13.10.12 14:49
*/

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.RivalryBreakRequest;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a RivalCommand
 */
public class RivalCommand extends GenericPlayerCommand {

    public RivalCommand(SimpleClans plugin)
    {
        super("Rival", plugin);
        setArgumentRange(2, 2);
        setUsages(Language.getTranslation("usage.rival", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("rival.command"));
        setPermission("simpleclans.leader.rival");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.isLeader() && cp.getClan().isVerified()) {
                return Language.getTranslation("menu.rival", plugin.getSettingsManager().getClanCommand());
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

            if (!clan.isVerified()) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                return;
            }

            if (!plugin.getSettingsManager().isUnRivalAble(clan)) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clan.cannot.create.rivals"));
                return;
            }

            if (clan.isLeader(cp)) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
                return;
            }

            if (clan.getSize() < plugin.getSettingsManager().getMinimalSizeToRival()) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("min.players.rivalries", plugin.getSettingsManager().getMinimalSizeToRival()));
                return;
            }

            String action = args[0];
            Clan rival = plugin.getClanManager().getClan(args[1]);

            if (rival == null) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.clan.matched"));
                return;
            }

            if (!plugin.getSettingsManager().isUnRivalAble(rival)) {
                if (!rival.isVerified()) {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("cannot.rival.an.unverified.clan"));
                    return;
                }

                if (action.equals(Language.getTranslation("add"))) {
                    if (clan.reachedRivalLimit()) {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("rival.limit.reached"));
                        return;
                    }

                    if (!clan.isRival(rival)) {
                        clan.addRival(rival);
                        rival.addBBMessage(cp, ChatColor.AQUA + Language.getTranslation("has.initiated.a.rivalry", clan.getName(), rival.getName()));
                        clan.addBBMessage(cp, ChatColor.AQUA + Language.getTranslation("has.initiated.a.rivalry", player.getName(), rival.getName()));
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.already.rivals"));
                    }

                } else if (action.equals(Language.getTranslation("remove"))) {
                    if (clan.isRival(rival)) {
                        Set<ClanPlayer> leaders = GeneralHelper.stripOfflinePlayers(clan.getLeaders());

                        plugin.getRequestManager().createRequest(new RivalryBreakRequest(leaders, cp, clan, rival));
                        ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("leaders.asked.to.end.rivalry", rival.getName()));
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("your.clans.are.not.rivals"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("usage.ally", plugin.getSettingsManager().getClanCommand()));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.clan.cannot.be.rivaled"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        }
    }
}