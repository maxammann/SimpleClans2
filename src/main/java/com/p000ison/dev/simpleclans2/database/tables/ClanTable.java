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

@Table("sc2_clans")
public class ClanTable {
    @Id
    public long id;

    @Field
    public String tag;

    @Field
    public String name;

    @Field
    public boolean verified;

    @Field
    public long founded;

    @Field
    public long last_action;

    @Field
    public String flags;

    @Field
    public boolean friendly_fire;


}
