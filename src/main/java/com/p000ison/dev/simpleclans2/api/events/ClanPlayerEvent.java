package com.p000ison.dev.simpleclans2.api.events;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a ClanPlayerEvent
 */
public class ClanPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final ClanPlayer clanPlayer;

    public ClanPlayerEvent(ClanPlayer clanPlayer)
    {
        this.clanPlayer = clanPlayer;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public ClanPlayer getClanPlayer()
    {
        return clanPlayer;
    }
}
