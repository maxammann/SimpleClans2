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
 *     Last modified: 11.10.12 15:51
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.MultipleRequest;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a PromoteRequest
 */
public class PromoteRequest extends MultipleRequest {

    private ClanPlayer targetPlayer;

    public PromoteRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, ClanPlayer targetPlayer)
    {
        super(plugin, acceptors, requester);
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void onRequesting()
    {
        sendAnnouncerMessage(Language.getTranslation("asking.for.the.promotion", requester.getName(), targetPlayer.getName()));
    }

    @Override
    public boolean onAccepted()
    {
        ClanPlayer cp = getRequester();
        Clan clan = requester.getClan();


        if (clan != null && clan.equals(cp.getClan())) {

            clan.addBBMessage(MessageFormat.format(Language.getTranslation("promoted.to.leader"), targetPlayer.getName()));
            clan.setLeader(targetPlayer);
            targetPlayer.update(true);
        }
        return true;
    }

    @Override
    public void onDenied()
    {
        announceMessage(Language.getTranslation("promotion.denied", targetPlayer.getName()));
    }
}