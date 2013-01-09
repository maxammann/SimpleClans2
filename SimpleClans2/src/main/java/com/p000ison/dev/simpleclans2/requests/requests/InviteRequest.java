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

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.SingleRequest;
import org.bukkit.ChatColor;

import java.text.MessageFormat;

/**
 * Represents a InviteRequest
 */
public class InviteRequest extends SingleRequest {

    public InviteRequest(SimpleClans plugin, ClanPlayer invited, ClanPlayer requester)
    {
        super(plugin, invited, requester);
    }

    @Override
    public void onRequesting()
    {
        sendAcceptorMessage(ChatColor.AQUA + Language.getTranslation("you.have.been.invited", getAcceptor().getName()));
    }

    @Override
    public boolean onAccepted()
    {
        Clan clan = requester.getClan();

        if (clan == null || getAcceptor() == null) {
            return false;
        }

        ClanPlayer acceptor = getAcceptor();

        clan.addMember(acceptor);
        acceptor.setLeader(false);
        acceptor.setClan(clan);

        clan.addBBMessage(MessageFormat.format(Language.getTranslation("joined.the.clan"), acceptor.getName()));
        plugin.serverAnnounce(MessageFormat.format(Language.getTranslation("has.joined"), acceptor.getName(), clan.getName()));

        acceptor.update(true);
        return true;
    }

    @Override
    public void onDenied()
    {
        sendRequesterMessage(ChatColor.DARK_RED + Language.getTranslation("membership.invitation", getAcceptor().getName()));
    }
}
