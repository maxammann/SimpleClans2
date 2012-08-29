/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.database.tables;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

import java.sql.Timestamp;

@Table("sc2_players")
public class ClanPlayerTable {

    @Id
    public int id;

    @Field
    public String name;

    @Field
    public boolean leader;

    @Field
    public boolean trusted;

    @Field
    public long last_seen;

    @Field
    public long join_date;

    @Field
    public long clan;

    @Field
    public boolean friendly_fire;

    @Field
    public int neutral_kills;

    @Field
    public int rival_kills;

    @Field
    public int civilian_kills;

    @Field
    public int deaths;

    @Field
    public String flags;

    @Field
    public String past_clans;


}
