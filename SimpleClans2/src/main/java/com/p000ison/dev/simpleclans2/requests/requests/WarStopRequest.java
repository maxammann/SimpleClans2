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
 *     Last modified: 02.11.12 21:02
 */

package com.p000ison.dev.simpleclans2.requests.requests;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.MultipleRequest;
import org.bukkit.ChatColor;

import java.util.Set;

/**
 * Represents a WarStartRequest
 */
public class WarStopRequest extends MultipleRequest {

    private Clan warring;

    public WarStopRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, Clan warring)
    {
        super(plugin, acceptors, requester);
        this.warring = warring;
    }

    @Override
    public void onRequesting()
    {
        sendAcceptorMessage(ChatColor.AQUA + Language.getTranslation("proposing.to.end.the.war", requester.getClan().getTag(), warring.getTag()));
    }

    @Override
    public boolean onAccepted()
    {
        ClanPlayer cp = getRequester();
        Clan clan = requester.getClan();

        if (warring != null && clan != null) {

            clan.removeWarringClan(warring);
            warring.removeWarringClan(clan);

            warring.addBBMessage(cp, Language.getTranslation("you.are.no.longer.at.war", warring.getName(), clan.getTag()));
            clan.addBBMessage(cp, Language.getTranslation("you.are.no.longer.at.war", clan.getName(), warring.getTag()));

            clan.update(true);
            warring.update(true);
        }
        return true;
    }

    @Override
    public void onDenied()
    {
        sendRequesterMessage(ChatColor.DARK_RED + Language.getTranslation("peace.agreement.denied", warring.getTag()));
        sendAcceptorMessage(ChatColor.DARK_RED + Language.getTranslation("peace.agreement.denied", requester.getClan().getTag()));
    }
}
