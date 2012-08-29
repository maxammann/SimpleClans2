/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.database;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.p000ison.dev.simpleclans2.database.tables.ClanPlayerTable;
import com.p000ison.dev.simpleclans2.database.tables.ClanTable;

/**
 * Represents a DatabaseManager. This handles all the database stuff.
 */
public class DatabaseManager {

    private Database db = null;

    public DatabaseManager() throws TableRegistrationException, ConnectionException
    {

        MySQLConfiguration config = new MySQLConfiguration();
        config.setHost("localhost");
        config.setUser("root");
        config.setPassword("");
        config.setDatabase("sc");
        config.setPort(3306);
        db = DatabaseFactory.createNewDatabase(config);

        db.registerTable(ClanTable.class);
        db.registerTable(ClanPlayerTable.class);

        db.connect();
    }

    /**
     * Checks if the database is connected.
     *
     * @return Weather the database is connected
     */
    public boolean isConnected()
    {
        if (db == null) {
            return false;
        }

        return db.isConnected();
    }

    /**
     * Retrieve the database
     *
     * @return The Database link.
     */
    public Database getDatabase()
    {
        return db;
    }
}
