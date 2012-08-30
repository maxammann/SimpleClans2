/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.data.DataManager;
import com.p000ison.dev.simpleclans2.data.KillEntry;
import com.p000ison.dev.simpleclans2.data.KillType;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin {

    private ClanManager clanManager;
    private DatabaseManager databaseManager;
    private ClanPlayerManager clanPlayerManager;

    @Override
    public void onEnable()
    {
        new Logging(getLogger());

        loadManagers();
    }

    private void loadManagers()
    {
        try {
            databaseManager = new DatabaseManager();
        } catch (TableRegistrationException e) {
            Logging.debug(e);
        } catch (ConnectionException e) {
            Logging.debug(e);
        }

        clanManager = new ClanManager(this);
        clanPlayerManager = new ClanPlayerManager(this);
    }

//    public static void main(String[] args) throws TableRegistrationException, ConnectionException
//    {
//        Clan clan = new Clan(null, 0);
//        clan.setTag("test");
//
//        Clan ally = new Clan(null, 5);
//        clan.setTag("ally");
//
//        ClanPlayer cp = new ClanPlayer(null);
//
//
//        ClanManager cm = new ClanManager();
//
//        cm.addClan(clan);
//        cm.addClan(ally);
//
//        cp.setClan(cm.getClan(0));
//
//        cm.getClan(0).getFlags().addAllyClan(5);
//
//        System.out.println(JSONUtil.collectionToJSON("test", cp.getClan().getFlags().getAllies()));
//
////        clan.setTag(String.valueOf(new Random().nextInt()));
////
////        DatabaseManager databaseManager = new DatabaseManager();
////
////        databaseManager.getDatabase().save(clan);
//    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, java.lang.String label, java.lang.String[] args)
    {
        DataManager d = new DataManager(this);
        d.addKill("asdf", "d", "dsf", "sdf", KillType.NEUTRAL, true);

        KillEntry r = databaseManager.getDatabase().select(KillEntry.class).where().equal("attacker", "asdf").execute().findOne();

        System.out.println(r);
        return true;
    }

    public Database getSimpleClansDatabase()
    {
        if (databaseManager == null) {
            return null;
        }
        return databaseManager.getDatabase();
    }

    public DatabaseManager getDatabaseManager()
    {
        return databaseManager;
    }

    public ClanManager getClanManager()
    {
        return clanManager;
    }

    public ClanPlayerManager getClanPlayerManager()
    {
        return clanPlayerManager;
    }
}
