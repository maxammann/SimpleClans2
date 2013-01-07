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

/**
 * Represents a MultipleRequest
 */
public abstract class SingleRequest extends AbstractRequest {
    private final ClanPlayer acceptor;
    private boolean accepted = false;

    public SingleRequest(SimpleClans plugin, ClanPlayer acceptor, ClanPlayer requester)
    {
        super(plugin, requester);
        this.acceptor = acceptor;
    }

    public ClanPlayer getAcceptor()
    {
        return acceptor;
    }

    @Override
    public boolean isClanPlayerInvolved(ClanPlayer clanPlayer)
    {
        return acceptor.equals(clanPlayer) || requester.equals(clanPlayer);
    }

    @Override
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
        throw new IllegalArgumentException("You can not abstain a SingleRequest!");
    }

    @Override
    public boolean isAcceptor(ClanPlayer clanPlayer)
    {
        return clanPlayer.equals(acceptor);
    }

    @Override
    public void announceMessage(String message)
    {
        sendAcceptorMessage(message);
        sendRequesterMessage(message);
    }

    @Override
    public void sendAcceptorMessage(String message)
    {
        Player acceptorPlayer = acceptor.toPlayer();

        if (acceptorPlayer != null) {
            ChatBlock.sendMessage(acceptorPlayer, message);
        }
    }

    @Override
    public void sendRequesterMessage(String message)
    {
        Player requesterPlayer = requester.toPlayer();

        if (requesterPlayer != null) {
            ChatBlock.sendMessage(requesterPlayer, message);
        }
    }

    @Override
    public int getTimesVoted()
    {
        return !accepted ? 0 : 1;
    }

    @Override
    public int getAcceptorsSize()
    {
        return 1;
    }

    @Override
    public boolean isClanPlayerInvolved(Player player)
    {
        return isAcceptor(player) || isRequester(player);
    }

    @Override
    public boolean isAcceptor(Player player)
    {
        return player.getName().equals(acceptor.getName());
    }
}
