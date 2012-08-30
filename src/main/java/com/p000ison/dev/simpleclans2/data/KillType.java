/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.data;

/**
 * Represents a KillType
 */
public enum KillType {

    CIVILIAN((byte) 0),
    NEUTRAL((byte) 1),
    RIVAL((byte) 2);

    private byte type;


    private KillType(byte type)
    {
        this.type = type;
    }

    public byte getType()
    {
        return type;
    }
}
