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
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.requests.MultipleRequest;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Represents a PromoteRequest
 */
public class DemoteRequest extends MultipleRequest {

    private ClanPlayer targetPlayer;

    public DemoteRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, ClanPlayer targetPlayer)
    {
        super(plugin, acceptors, requester);
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void onRequesting()
    {
        sendAnnouncerMessage(ChatColor.AQUA + Language.getTranslation("asking.for.the.demotion", requester.getName(), targetPlayer.getName()));
    }

    @Override
    public boolean onAccepted()
    {
        Clan clan = requester.getClan();

        clan.addBBMessage(ChatColor.AQUA + MessageFormat.format(Language.getTranslation("demoted.back.to.member"), targetPlayer.getName()));
        clan.demote(targetPlayer);
        targetPlayer.update(true);

        return true;
    }

    @Override
    public void onDenied()
    {
         sendAnnouncerMessage(ChatColor.DARK_RED + Language.getTranslation("demotion.denied"));
    }
}