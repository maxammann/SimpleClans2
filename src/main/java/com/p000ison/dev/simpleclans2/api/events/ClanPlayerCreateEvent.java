package com.p000ison.dev.simpleclans2.api.events;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

/**
 * Represents a ClanPlayerCreateEvent
 */
public class ClanPlayerCreateEvent extends ClanPlayerEvent {

    public ClanPlayerCreateEvent(ClanPlayer clanPlayer)
    {
        super(clanPlayer);
    }
}
