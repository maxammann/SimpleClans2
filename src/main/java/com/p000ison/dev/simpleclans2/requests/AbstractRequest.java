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
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

/**
 * Represents a AbstractRequest
 */
public abstract class AbstractRequest implements Request {

    protected ClanPlayer requester;
    private String message;
    private long created;
    protected SimpleClans plugin;

    protected AbstractRequest(SimpleClans plugin, ClanPlayer requester, String message)
    {
        this.plugin = plugin;
        this.requester = requester;
        this.message = message;
        this.created = System.currentTimeMillis();
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public ClanPlayer getRequester()
    {
        return requester;
    }

    @Override
    public long getCreatedDate()
    {
        return created;
    }
}
