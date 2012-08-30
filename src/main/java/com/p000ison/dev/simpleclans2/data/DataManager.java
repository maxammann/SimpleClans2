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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ClanFlags;
import com.p000ison.dev.simpleclans2.database.tables.ClanTable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Represents a DataManager
 */
public class DataManager {

    private SimpleClans plugin;

    public DataManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public void importClans()
    {
        List<ClanTable> rawClans = plugin.getDatabaseManager().getDatabase().select(ClanTable.class).execute().find();
        Set<Clan> clans = new HashSet<Clan>();

        for (ClanTable rawClan : rawClans) {
            Clan clan = new Clan(plugin, rawClan.id, rawClan.tag, rawClan.name);

            clan.setFoundedDate(rawClan.founded);
            clan.setFriendlyFire(rawClan.friendly_fire);
            clan.setVerified(rawClan.verified);
            clan.setLastActionDate(rawClan.last_action);

            ClanFlags flags = new ClanFlags();
            flags.write(rawClan.flags);

            clan.setFlags(flags);

            clans.add(clan);
        }

        checkClans(clans);

        plugin.getClanManager().importClans(clans);
    }

    public void checkClans(Set<Clan> clans)
    {
        Iterator<Clan> it = clans.iterator();

        while (it.hasNext()) {
            Clan clan = it.next();

            if (clan.getInactiveDays() > 7) {
                purgeClan(clan.getId());
                it.remove();
            }
        }
    }

    public void purgeClan(long id)
    {
        ClanTable clanTable = new ClanTable();
        clanTable.id = id;
        plugin.getDatabaseManager().getDatabase().remove(clanTable);
    }


    public void addKill(String attacker, String attackerTag, String victim, String victimTag, KillType type, boolean inWar)
    {
        KillEntry kill = new KillEntry();

        kill.setAttacker(attacker);
        kill.setAttackerTag(attackerTag);
        kill.setVictim(victim);
        kill.setVictimTag(victimTag);
        kill.setKillType(type.getType());
        kill.setDate(System.currentTimeMillis());
        kill.setWar(inWar);

        plugin.getDatabaseManager().getDatabase().save(kill);
    }
}
