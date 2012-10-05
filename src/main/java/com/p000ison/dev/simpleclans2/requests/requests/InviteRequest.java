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
 *     Created: 12.09.12 13:38
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.SingleAcceptorRequest;
import com.p000ison.dev.simpleclans2.util.Announcer;

import java.text.MessageFormat;

/**
 * Represents a InviteRequest
 */
public class InviteRequest extends SingleAcceptorRequest {


    public InviteRequest(ClanPlayer invited, ClanPlayer requester, Clan clan)
    {
        super(invited, requester, clan, MessageFormat.format(Language.getTranslation("you.have.been.invited"), clan.getTag()));
    }

    @Override
    public boolean execute()
    {
        if (getClan() == null || getAcceptor() == null) {
            return false;
        }

        getClan().addBBMessage(MessageFormat.format(Language.getTranslation("joined.the.clan"), getAcceptor().getName()));
        Announcer.announce(MessageFormat.format(Language.getTranslation("has.joined"), getAcceptor().getName(), getClan().getName()));
        getClan().addMember(getAcceptor());
        return true;
    }
}
