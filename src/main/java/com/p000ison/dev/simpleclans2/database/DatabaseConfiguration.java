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
 *     Created: 10/2/12 5:10 PM
 */

package com.p000ison.dev.simpleclans2.database;

/**
 * Represents a DatabaseConfiguration
 */
public class DatabaseConfiguration {

    private String host, username, password, database;
    private DatabaseMode mode;
    private int port;


    public DatabaseConfiguration()
    {
    }

    public DatabaseConfiguration(String host, String username, String password, String database, String mode, int port)
    {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.mode = DatabaseMode.getMode(mode);
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDatabase()
    {
        return database;
    }

    public DatabaseMode getMode()
    {
        return mode;
    }

    public int getPort()
    {
        return port;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setDatabase(String database)
    {
        this.database = database;
    }

    public void setMode(String mode)
    {
        this.mode = DatabaseMode.getMode(mode);
    }

    public void setPort(int port)
    {
        this.port = port;
    }
}
