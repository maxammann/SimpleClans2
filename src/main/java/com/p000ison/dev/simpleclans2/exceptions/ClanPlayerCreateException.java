package com.p000ison.dev.simpleclans2.exceptions;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

/**
 * Represents a ClanPlayerCreateException
 */
public class ClanPlayerCreateException extends RuntimeException {

    public ClanPlayerCreateException(ClanPlayer clanPlayer, long id)
    {
        super(String.format("Failed to create ClanPlayer: %s", clanPlayer == null ? id : clanPlayer));
    }
}
