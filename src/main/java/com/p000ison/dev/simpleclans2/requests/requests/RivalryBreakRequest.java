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
 *     Last modified: 13.10.12 18:41
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
 * Represents a AllyCreateRequest
 */
public class RivalryBreakRequest extends MultipleRequest {

    private Clan rival;

    public RivalryBreakRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, Clan rival)
    {
        super(plugin, acceptors, requester);
        this.rival = rival;
    }

    @Override
    public void onRequesting()
    {
        sendAnnouncerMessage(ChatColor.AQUA + Language.getTranslation("proposing.to.end.the.rivalry", requester.getClan().getTag(), rival.getTag()));
    }

    @Override
    public boolean onAccepted()
    {
        ClanPlayer cp = getRequester();
        Clan clan = requester.getClan();


        if (rival != null && clan != null) {

            clan.removeRival(rival);
            rival.removeRival(clan);

            rival.addBBMessage(cp, Language.getTranslation("broken.the.rivalry", getAcceptors().size(), clan.getName()));
            clan.addBBMessage(cp, Language.getTranslation("broken.the.rivalry.with", cp.getName(), rival.getName()));

            clan.update(true);
            rival.update(true);
        }
        return true;
    }

    @Override
    public void onDenied()
    {
        sendRequesterMessage(ChatColor.DARK_RED + Language.getTranslation("rivalry.request.denied", rival.getTag()));
        sendAnnouncerMessage(ChatColor.DARK_RED + Language.getTranslation("rivalry.request.denied", requester.getClan().getTag()));
    }
}
