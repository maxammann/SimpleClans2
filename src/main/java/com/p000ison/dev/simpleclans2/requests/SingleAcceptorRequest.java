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
 *     Created: 05.09.12 19:08
 */

package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.entity.Player;

/**
 * Represents a MultipleAcceptorsRequest
 */
public abstract class SingleAcceptorRequest extends Request {
    private final ClanPlayer acceptor;

    public SingleAcceptorRequest(ClanPlayer acceptor, ClanPlayer requester, Clan clan, String message)
    {
        super(requester, clan, message);
        this.acceptor = acceptor;
    }

    public ClanPlayer getAcceptor()
    {
        return acceptor;
    }

    public void processRequest()
    {
        acceptor.toPlayer().sendMessage("accepted");
        acceptor.setLastVoteResult(VoteResult.UNKNOWN);

        execute();
        getRequester().toPlayer().sendMessage("accept");
    }

    @Override
    public boolean hasRequestToHandle(ClanPlayer clanPlayer)
    {
        return acceptor.equals(clanPlayer) || requester.equals(clanPlayer);
    }

    public void cancelRequest()
    {
        acceptor.toPlayer().sendMessage("Cancelled");
        acceptor.setLastVoteResult(VoteResult.UNKNOWN);

        getRequester().toPlayer().sendMessage("Cancelledbo");
    }

    public boolean checkRequest()
    {
        return !getAcceptor().getLastVoteResult().equals(VoteResult.ACCEPT);
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return !acceptor.getLastVoteResult().equals(VoteResult.UNKNOWN);
    }

    @Override
    public void deny(ClanPlayer clanPlayer)
    {
        clanPlayer.setLastVoteResult(VoteResult.DENY);
    }

    @Override
    public void accept(ClanPlayer clanPlayer)
    {
        clanPlayer.setLastVoteResult(VoteResult.ACCEPT);
    }

    @Override
    public void abstain(ClanPlayer clanPlayer)
    {
        clanPlayer.setLastVoteResult(VoteResult.ABSTAINED);
    }

    public void sendRequest()
    {
        Player player = getAcceptor().toPlayer();

        if (player == null) {
            return;
        }

        getAcceptor().toPlayer().sendMessage(getMessage());
    }
}
