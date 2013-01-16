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
import com.p000ison.dev.simpleclans2.requests.MultipleRequest;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a AllyCreateRequest
 */
public class AllyCreateRequest extends MultipleRequest {

    private Clan ally;

    public AllyCreateRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, Clan ally) {
        super(plugin, acceptors, requester);
        this.ally = ally;
    }

    @Override
    public void onRequesting() {
        sendAcceptorMessage(ChatColor.AQUA + Language.getTranslation("proposing.an.alliance", requester.getClan().getTag(), ally.getTag()));
    }

    @Override
    public boolean onAccepted() {
        Clan clan = requester.getClan();

        if (ally != null && clan != null) {
            clan.addAlly(ally);
            ally.addAlly(clan);

            ally.addBBMessage(ally, MessageFormat.format(Language.getTranslation("accepted.an.alliance"), ally.getName(), clan.getName()));
            clan.addBBMessage(clan, MessageFormat.format(Language.getTranslation("alliances.created.own.clan"), ally.getName()));

            clan.update(true);
            ally.update(true);
        }
        return true;
    }

    @Override
    public void onDenied() {
        sendRequesterMessage(ChatColor.DARK_RED + Language.getTranslation("the.alliance.was.denied", ally.getTag()));
        sendAcceptorMessage(ChatColor.DARK_RED + Language.getTranslation("the.alliance.was.denied", requester.getClan().getTag()));
    }
}
