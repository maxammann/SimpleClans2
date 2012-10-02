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
 *     Created: 05.09.12 13:19
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.requests.MultipleAcceptorsRequest;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a AllyCreateRequest
 */
public class AllyCreateRequest extends MultipleAcceptorsRequest {

    private Clan ally;

    public AllyCreateRequest(Set<ClanPlayer> acceptors, ClanPlayer requester, Clan clan, Clan ally)
    {
        super(acceptors, requester, clan, MessageFormat.format(Language.getTranslation("proposing.an.alliance"), clan.getName(), ally.getTag()));
        this.ally = ally;
    }

    @Override
    public boolean execute()
    {
        ClanPlayer cp = getRequester();
        Clan clan = getClan();


        if (ally != null && clan != null) {

            clan.addAlly(ally);
            ally.addAlly(clan);

            ally.addBBMessage(cp, MessageFormat.format(Language.getTranslation("accepted.an.alliance"), getAcceptors(), clan.getName()));
            clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("created.an.alliance"), cp.getName(), ally.getName()));
        }
        return true;
    }
}
