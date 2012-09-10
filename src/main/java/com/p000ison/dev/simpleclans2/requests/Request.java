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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

/**
 * Represents a Request
 */
public abstract class Request implements Executable {

    protected ClanPlayer requester;
    private Clan clan;
    private String message;
    private long created;


    public Request(ClanPlayer requester, Clan clan, String message)
    {
        this.requester = requester;
        this.clan = clan;
        this.message = message;
        this.created = System.currentTimeMillis();
    }

    public Clan getClan()
    {
        return clan;
    }

    public String getMessage()
    {
        return message;
    }

    public ClanPlayer getRequester()
    {
        return requester;
    }

    public long getCreatedDate()
    {
        return created;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public abstract void accept(ClanPlayer clanPlayer);

    public abstract void deny(ClanPlayer clanPlayer);

    public abstract void abstain(ClanPlayer clanPlayer);

    public abstract boolean hasEveryoneVoted();

    public abstract void processRequest();

    public abstract void cancelRequest();

    public abstract boolean checkRequest();

    public abstract void sendRequest();

    public abstract boolean isAcceptor(ClanPlayer clanPlayer);

    public abstract void announceMessage(String message);

    public abstract boolean hasRequestToHandle(ClanPlayer clanPlayer);


}
