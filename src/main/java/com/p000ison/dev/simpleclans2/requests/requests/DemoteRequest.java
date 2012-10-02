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
 *     Created: 10.09.12 17:07
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.requests.MultipleAcceptorsRequest;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a PromoteRequest
 */
public class DemoteRequest extends MultipleAcceptorsRequest {

    private ClanPlayer targetPlayer;

    public DemoteRequest(Set<ClanPlayer> acceptors, ClanPlayer requester, Clan clan, ClanPlayer targetPlayer)
    {
        super(acceptors, requester, clan, MessageFormat.format(Language.getTranslation("asking.for.the.demotion"), requester.getName(), targetPlayer.getName()));
        this.targetPlayer = targetPlayer;
    }

    @Override
    public boolean execute()
    {
        Clan clan = getClan();

        clan.addBBMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("demoted.back.to.member"), targetPlayer));
        clan.demote(targetPlayer);

        return true;
    }
}