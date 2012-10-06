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
 *     Created: 09.09.12 12:56
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.MultipleAcceptorsRequest;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a PromoteRequest
 */
public class PromoteRequest extends MultipleAcceptorsRequest {

    private ClanPlayer targetPlayer;

    public PromoteRequest(Set<ClanPlayer> acceptors, ClanPlayer requester, Clan clan, ClanPlayer targetPlayer)
    {
        super(acceptors, requester, clan, MessageFormat.format(Language.getTranslation("asking.for.the.promotion"), requester.getName(), targetPlayer));
        this.targetPlayer = targetPlayer;
    }

    @Override
    public boolean execute()
    {
        ClanPlayer cp = getRequester();
        Clan clan = getClan();


        if (clan != null && clan.equals(cp.getClan())) {


            clan.addBBMessage(MessageFormat.format(Language.getTranslation("promoted.to.leader"), targetPlayer.getName()));
            clan.setLeader(targetPlayer);
            targetPlayer.update(true);

//            else
//            {
//                String deniers = Helper.capitalize(Helper.toMessage(Helper.toArray(denies), ", "));
//                clan.leaderAnnounce(ChatColor.RED + MessageFormat.format(plugin.getLang("denied.the.promotion"), deniers, promoted));
//            }
        }
        return true;
    }
}