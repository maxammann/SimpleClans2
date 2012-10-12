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

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a MultipleAcceptorsRequest
 */
public abstract class MultipleAcceptorsRequest extends Request {
    private final Set<ClanPlayer> acceptors;
    private Set<ClanPlayer> accepts = new HashSet<ClanPlayer>();
    private Set<ClanPlayer> denies = new HashSet<ClanPlayer>();
    private Set<ClanPlayer> abstains = new HashSet<ClanPlayer>();

    public MultipleAcceptorsRequest(Set<ClanPlayer> acceptors, ClanPlayer requester, Clan clan, String message)
    {
        super(requester, clan, message);
        this.acceptors = acceptors;
    }

    public Set<ClanPlayer> getAcceptors()
    {
        return Collections.unmodifiableSet(acceptors);
    }

    @Override
    public boolean hasRequestToHandle(ClanPlayer clanPlayer)
    {
        return requester.equals(clanPlayer) || acceptors.contains(clanPlayer);
    }

    public void processRequest()
    {
        for (ClanPlayer acceptor : getAcceptors()) {
            acceptor.toPlayer().sendMessage("accepted");
            acceptor.setLastVoteResult(VoteResult.UNKNOWN);
        }

        execute();
        getRequester().toPlayer().sendMessage("accept");
    }

    public void cancelRequest()
    {

        for (ClanPlayer acceptor : getAcceptors()) {
            acceptor.toPlayer().sendMessage("Cancelled");
            acceptor.setLastVoteResult(VoteResult.UNKNOWN);
        }

        getRequester().toPlayer().sendMessage("Cancelledbo");
    }

    @Override
    public void accept(ClanPlayer clanPlayer)
    {
        accepts.add(clanPlayer);
    }

    @Override
    public void deny(ClanPlayer clanPlayer)
    {
        denies.add(clanPlayer);
    }

    @Override
    public void abstain(ClanPlayer clanPlayer)
    {
        abstains.add(clanPlayer);
    }

    public boolean checkRequest()
    {
        return accepts.size() * 2 > acceptors.size();
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return accepts.size() + denies.size() + abstains.size() >= acceptors.size();
    }

    public int getAccepts()
    {
        return accepts.size();
    }

    public int getDenies()
    {
        return denies.size();
    }

    @Override
    public void announceMessage(String message)
    {
        for (ClanPlayer clanPlayer : acceptors) {
            Player player = clanPlayer.toPlayer();

            if (player == null) {
                continue;
            }

            player.sendMessage(message);
        }

        Player player = requester.toPlayer();

        if (player != null) {
            player.sendMessage(message);
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
