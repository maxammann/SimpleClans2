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
 *     Last modified: 05.11.12 20:53
 */

package com.p000ison.dev.simpleclans2.converter;

import com.p000ison.dev.simpleclans2.database.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a converter.Converter
 */
@SuppressWarnings("unchecked")
public class Converter {

    private Database from;
    private Database to;
    private PreparedStatement insertClan, insertBB, updateClan, insertKill;
    private PreparedStatement insertClanPlayer;
    private Set<ConvertedClan> clans = new HashSet<ConvertedClan>();

    public Converter(Database from, Database to)
    {
        this.from = from;
        this.to = to;

        insertClan = to.prepareStatement("INSERT INTO `sc2_clans` (`name`, `tag`, `verified`, `founded`, `last_action`, `flags` ) VALUES ( ?, ?, ?, ?, ?, ? );");
        insertBB = to.prepareStatement("INSERT INTO `sc2_bb` (`clan`, `text` ) VALUES ( ?, ? );");
        updateClan = to.prepareStatement("UPDATE `sc2_clans` SET allies = ?, rivals = ?, warring = ? WHERE id = ?;");
        insertClanPlayer = to.prepareStatement("INSERT INTO `sc2_players` ( `name`, `leader`, `trusted`, `join_date`, `last_seen`, `clan`, `neutral_kills`, `rival_Kills`, `civilian_Kills`, `deaths`, `flags` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
        insertKill = to.prepareStatement("INSERT INTO `sc2_kills` ( `attacker`, `attacker_tag`, `victim`, `victim_tag`, `war`, `type`, `date` ) VALUES ( ?, ?, ?, ?, ?, ?, ? );");
    }

    public void convertAll() throws SQLException
    {
        try {
            convertClans();
            convertPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        convertKills();
    }

    public void convertPlayers() throws SQLException
    {
        ResultSet result = from.query("SELECT * FROM `sc_players`;");

        while (result.next()) {
            boolean friendlyFire = result.getBoolean("friendly_fire");
            String flags = null;
            if (friendlyFire) {
                JSONObject JSONflags = new JSONObject();
                JSONflags.put("ff", friendlyFire);
                flags = JSONflags.toJSONString();
            }
            insertPlayer(result.getString("name"), result.getBoolean("leader"), result.getBoolean("trusted"), result.getLong("join_date"), result.getLong("last_seen"), getIDByTag(result.getString("tag")), result.getBoolean("friendly_fire"), result.getInt("neutral_kills"), result.getInt("rival_kills"), result.getInt("civilian_kills"), result.getInt("deaths"), flags);
        }
        clans = null;
    }

    public void insertPlayer(String name, boolean leader, boolean trusted, long joinDate, long lastSeen, long clan, boolean friendlyFire, int neutralKills, int rivalKills, int civilianKills, int deaths, String flags) throws SQLException
    {
        insertClanPlayer.setString(1, name);
        insertClanPlayer.setBoolean(2, leader);
        insertClanPlayer.setBoolean(3, trusted);
        insertClanPlayer.setTimestamp(4, new Timestamp(joinDate));
        insertClanPlayer.setTimestamp(5, new Timestamp(lastSeen));
        insertClanPlayer.setLong(6, clan);
        insertClanPlayer.setInt(7, neutralKills);
        insertClanPlayer.setInt(8, rivalKills);
        insertClanPlayer.setInt(9, civilianKills);
        insertClanPlayer.setInt(10, deaths);

        if (flags != null) {
            insertClanPlayer.setString(11, flags);
        } else {
            insertClanPlayer.setNull(11, Types.VARCHAR);
        }


        insertClanPlayer.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public void convertClans() throws SQLException
    {
        ResultSet result = from.query("SELECT * FROM `sc_clans`;");
        JSONObject flags = new JSONObject();

        while (result.next()) {
            String name = result.getString("name");
            String tag = result.getString("tag");
            boolean verified = result.getBoolean("verified");
            boolean friendly_fire = result.getBoolean("friendly_fire");
            long founded = result.getLong("founded");
            long last_used = result.getLong("last_used");
            String flagsString = result.getString("flags");
            String cape = result.getString("cape_url");

            ConvertedClan clan = new ConvertedClan(tag);
            clan.setPackedAllies(result.getString("packed_allies"));
            clan.serPackedRivals(result.getString("packed_rivals"));

            if (friendly_fire) {
                flags.put("ff", friendly_fire);
            }

            if (cape != null && !cape.isEmpty()) {
                flags.put("cape", cape);
            }

            JSONParser parser = new JSONParser();
            try {
                JSONObject object = (JSONObject) parser.parse(flagsString);
                String world = object.get("homeWorld").toString();
                if (!world.isEmpty()) {
                    int x = ((Long) object.get("homeX")).intValue();
                    int y = ((Long) object.get("homeY")).intValue();
                    int z = ((Long) object.get("homeZ")).intValue();

                    flags.put("home", x + ":" + y + ":" + z + ":" + world + ":0:0");
                }

                clan.setRawWarring((JSONArray) object.get("warring"));

            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            insertClan(name, tag, verified, founded, last_used, flags.isEmpty() ? null : flags.toJSONString());

            String selectLastQuery = "SELECT `id` FROM `sc2_clans` ORDER BY ID DESC LIMIT 1;";

            ResultSet selectLast = to.query(selectLastQuery);
            selectLast.next();
            clan.setId(selectLast.getLong("id"));
            selectLast.close();

            insertBB(Arrays.asList(result.getString("packed_bb").split("\\s*(\\||$)")), clan.getId());

            clans.add(clan);
        }

        for (ConvertedClan clan : clans) {
            JSONArray allies = new JSONArray();
            JSONArray rivals = new JSONArray();
            JSONArray warring = new JSONArray();

            for (String allyTag : clan.getRawAllies()) {
                long allyID = getIDByTag(allyTag);
                if (allyID != -1) {
                    allies.add(allyID);
                }
            }

            for (String rivalTag : clan.getRawAllies()) {
                long rivalID = getIDByTag(rivalTag);
                if (rivalID != -1) {
                    rivals.add(rivalID);
                }
            }

            for (String warringTag : clan.getRawWarring()) {
                long warringID = getIDByTag(warringTag);
                if (warringID != -1) {
                    warring.add(warringID);
                }
            }

            if (!allies.isEmpty()) {
                updateClan.setString(1, allies.toJSONString());
            } else {
                updateClan.setNull(1, Types.VARCHAR);
            }

            if (!rivals.isEmpty()) {
                updateClan.setString(2, rivals.toJSONString());
            } else {
                updateClan.setNull(2, Types.VARCHAR);
            }

            if (!warring.isEmpty()) {
                updateClan.setString(3, warring.toJSONString());
            } else {
                updateClan.setNull(3, Types.VARCHAR);
            }

            updateClan.setLong(4, clan.getId());
            updateClan.executeUpdate();
        }
    }


    public long getIDByTag(String tag)
    {
        for (ConvertedClan clan : clans) {
            if (clan.getTag().equals(tag)) {
                return clan.getId();
            }
        }

        return -1;
    }

    public void insertBB(List<String> bb, long clan) throws SQLException
    {
        for (String text : bb) {
            insertBB.setLong(1, clan);
            insertBB.setString(2, text);
            insertBB.executeUpdate();
        }
    }

    public void insertClan(String name, String tag, boolean verified, long founded, long last_action, String flags) throws SQLException
    {
        insertClan.setString(1, name);
        insertClan.setString(2, tag);
        insertClan.setBoolean(3, verified);
        insertClan.setTimestamp(4, new Timestamp(founded));
        insertClan.setTimestamp(5, new Timestamp(last_action));
        if (flags != null) {
            insertClan.setString(6, flags);
        } else {
            insertClan.setNull(6, Types.VARCHAR);
        }

        insertClan.executeUpdate();
    }

    public void convertKills() throws SQLException
    {
        ResultSet result = from.query("SELECT * FROM `sc_kills`;");

        while (result.next()) {

        }
    }

    public void insertKill()
    {
        //`attacker`, `attacker_tag`, `victim`, `victim_tag`, `war`, `type`, `date`

    }
}
