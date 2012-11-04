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
import org.bukkit.entity.Player;

/**
 * Represents a MultipleAcceptorsRequest
 */
public abstract class SingleAcceptorRequest extends AbstractRequest {
    private final ClanPlayer acceptor;
    private boolean accepted = false;

    public SingleAcceptorRequest(SimpleClans plugin, ClanPlayer acceptor, ClanPlayer requester, String message)
    {
        super(plugin, requester, message);
        this.acceptor = acceptor;
    }

    public ClanPlayer getAcceptor()
    {
        return acceptor;
    }

    public void processRequest()
    {
        accepted = false;

        execute();
    }

    @Override
    public boolean isClanPlayerInvolved(ClanPlayer clanPlayer)
    {
        return acceptor.equals(clanPlayer) || requester.equals(clanPlayer);
    }

    public boolean checkRequest()
    {
        return accepted;
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return accepted;
    }

    @Override
    public void deny()
    {
        accepted = false;
    }

    @Override
    public boolean isClanInvolved(Clan clan)
    {
        Clan acceptorClan = acceptor.getClan();
        Clan requesterClan = requester.getClan();

        return clan.equals(acceptorClan) && !clan.equals(requesterClan);
    }

    @Override
    public void accept()
    {
        accepted = true;
    }

    @Override
    public void abstain()
    {
        throw new IllegalArgumentException("You can not abstain a SingleAcceptorRequest!");
    }

    public void sendRequest()
    {
        Player player = getAcceptor().toPlayer();

        if (player == null) {
            return;
        }

        getAcceptor().toPlayer().sendMessage(getMessage());
    }

    @Override
    public boolean isAcceptor(ClanPlayer clanPlayer)
    {
        return clanPlayer.equals(acceptor);
    }

    @Override
    public void announceMessage(String message)
    {
        Player acceptorPlayer = acceptor.toPlayer();

        if (acceptorPlayer != null) {
            acceptorChatBlock.sendMessage(player, message);
        }

        Player requesterPlayer = requester.toPlayer();

        if (requesterPlayer != null) {
            requesterChatBlock.sendMessage(player, message);
        }
    }
}
