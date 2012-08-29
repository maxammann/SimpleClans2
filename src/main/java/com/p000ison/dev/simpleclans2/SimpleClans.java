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

import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin {

    private ClanManager clanManager;
    private DatabaseManager databaseManager;

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

        clanManager = new ClanManager();
    }

    public DatabaseManager getDatabaseManager()
    {
        return databaseManager;
    }

    public ClanManager getClanManager()
    {
        return clanManager;
    }
}
