package com.p000ison.dev.simpleclans2.api.events;

import com.p000ison.dev.simpleclans2.clan.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a ClanPlayerEvent
 */
public class ClanEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Clan clan;

    public ClanEvent(Clan clan)
    {
        this.clan = clan;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public Clan getClan()
    {
        return clan;
    }
}
