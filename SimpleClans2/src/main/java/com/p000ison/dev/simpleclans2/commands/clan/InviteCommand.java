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
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.logging.Logging;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.requests.InviteRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.logging.Level;


public class InviteCommand extends GenericPlayerCommand {


    public InviteCommand(SimpleClans plugin) {
        super("Invite", plugin);
        setArgumentRange(1, 1);
        setUsages(Language.getTranslation("usage.invite"));
        setIdentifiers(Language.getTranslation("invite.command"));
        setPermission("simpleclans.leader.invite");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null && cp.isLeader()) {
            return Language.getTranslation("menu.invite");
        }

        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer cp = plugin.getClanPlayerManager().getCreateClanPlayerExact(player);

        if (cp == null) {
            final String error = "Failed to invite because the creation of the player failed!";
            ChatBlock.sendMessage(player, ChatColor.RED + error);
            Logging.debug(Level.SEVERE, error);
            return;
        }
        Clan clan = cp.getClan();

        if (!clan.isLeader(cp) && !cp.hasRankPermission("manage.invites")) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        Player invited = Bukkit.getPlayer(args[0]);

        if (invited == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("no.player.matched"));
            return;
        }

        if (!invited.hasPermission("simpleclans.member.can-join")) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.doesn.t.not.have.the.permissions.to.join.clans"));
            return;
        }

        if (invited.equals(player)) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.cannot.invite.yourself"));
            return;
        }

        if (cp.isBanned()) {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("this.player.is.banned.from.using.clan.commands"));
            return;
        }

        ClanPlayer invitedClanPlayer = plugin.getClanPlayerManager().createClanPlayer(invited);

        if (invitedClanPlayer.getClan() == null) {

            if (SimpleClans.hasEconomy() && plugin.getSettingsManager().isPurchaseInvite() && !cp.withdraw(plugin.getSettingsManager().getInvitationPrice())) {
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
                return;
            }

            plugin.getRequestManager().createRequest(new InviteRequest(plugin, invitedClanPlayer, cp));
            ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("has.been.asked.to.join"), invited.getName(), clan.getName()));

        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("the.player.is.already.member.of.another.clan"));
        }


    }
}
