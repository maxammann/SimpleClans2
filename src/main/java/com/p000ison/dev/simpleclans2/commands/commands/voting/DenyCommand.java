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
 *     Created: 06.09.12 22:05
 */

package com.p000ison.dev.simpleclans2.commands.commands.voting;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.requests.Request;
import com.p000ison.dev.simpleclans2.requests.VoteResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a AcceptCommand
 */
public class DenyCommand extends GenericPlayerCommand {

    public DenyCommand(SimpleClans plugin)
    {
        super("Deny", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.deny"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("deny.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer, CommandSender sender)
    {
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            player.sendMessage(Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (clan == null) {
            player.sendMessage(Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Request request = plugin.getRequestManager().vote(clanPlayer, VoteResult.DENY);

        if (request != null) {
            request.announceMessage(Language.getTranslation("voted.to.deny"));
        } else {
            player.sendMessage("nothing.to.accept");
        }
    }

}
