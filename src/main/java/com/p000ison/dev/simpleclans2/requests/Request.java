/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */


package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a Request
 */
public class Request {

    private final Set<ClanPlayer> acceptors;
    private ClanPlayer requester;
    private Clan clan;
    private String message;
    private Runnable goal;

    public Request(Set<ClanPlayer> acceptors, ClanPlayer requester/*, String target*/, Clan clan, String message, Runnable goal)
    {
        this.acceptors = acceptors;
        this.requester = requester;
        this.clan = clan;
        this.message = message;
    }

    public Set<ClanPlayer> getAcceptors()
    {
        return Collections.unmodifiableSet(acceptors);
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

    public void runGoal()
    {
        goal.run();
    }
}
