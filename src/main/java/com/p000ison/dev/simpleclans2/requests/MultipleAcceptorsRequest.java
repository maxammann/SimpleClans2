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

package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a MultipleAcceptorsRequest
 */
public abstract class MultipleAcceptorsRequest extends AbstractRequest {
    private final Set<ClanPlayer> acceptors;
    private short accepts = 0;
    private short denies = 0;
    private short abstains = 0;

    public MultipleAcceptorsRequest(SimpleClans plugin, Set<ClanPlayer> acceptors, ClanPlayer requester, String message)
    {
        super(plugin, requester, message);
        this.acceptors = acceptors;
    }

    public Set<ClanPlayer> getAcceptors()
    {
        return Collections.unmodifiableSet(acceptors);
    }

    @Override
    public boolean isClanPlayerInvolved(ClanPlayer clanPlayer)
    {
        return requester.equals(clanPlayer) || acceptors.contains(clanPlayer);
    }

    public void processRequest()
    {
        execute();
    }

    @Override
    public boolean isClanInvolved(Clan clan)
    {
        Clan requesterClan = requester.getClan();

        if (clan.equals(requesterClan)) {
            return true;
        }

        for (ClanPlayer clanPlayer : acceptors) {
            if (clanPlayer == null) {
                continue;
            }

            if (clan.equals(clanPlayer.getClan())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void accept()
    {
        accepts++;
    }

    @Override
    public void deny()
    {
        denies++;
    }

    @Override
    public void abstain()
    {
        abstains++;
    }

    public boolean checkRequest()
    {
        return accepts * 2 > acceptors.size();
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return accepts + denies + abstains >= acceptors.size();
    }

    public int getAccepts()
    {
        return accepts;
    }

    public int getAbstains()
    {
        return abstains;
    }

    public int getDenies()
    {
        return denies;
    }

    @Override
    public void announceMessage(String message)
    {
        for (ClanPlayer clanPlayer : acceptors) {
            Player player = clanPlayer.toPlayer();

            if (player == null) {
                continue;
            }

            ChatBlock.sendMessage(player, message);
        }

        Player player = requester.toPlayer();

        if (player != null) {
            ChatBlock.sendMessage(player, message);
        }
    }

    public void sendRequest()
    {
        announceMessage(getMessage());
    }

    @Override
    public boolean isAcceptor(ClanPlayer clanPlayer)
    {
        return acceptors.contains(clanPlayer);
    }
}
