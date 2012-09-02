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


package com.p000ison.dev.simpleclans2.database.tables;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

@Table("sc2_players")
public class ClanPlayerTable {

    @Id
    public long id;

    @Field
    public String name;

    @Field
    public boolean leader;

    @Field
    public String rank;

    @Field
    public boolean trusted;

    @Field
    public boolean banned;

    @Field
    public long last_seen;

    @Field
    public long join_date;

    @Field
    public long clan = -1L;

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
