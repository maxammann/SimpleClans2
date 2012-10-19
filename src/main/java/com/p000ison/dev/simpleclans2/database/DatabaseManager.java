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
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.database;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Represents a DatabaseManager
 */
public class DatabaseManager {

    private SimpleClans plugin;

    private Database database;

    public DatabaseManager(SimpleClans plugin) throws SQLException
    {
        this.plugin = plugin;

        init();
    }

    private void init() throws SQLException
    {
        database = new MySQLDatabase(plugin.getSettingsManager().getDatabaseConfiguration());

        if (!database.checkConnection()) {
            Logging.debug(Level.SEVERE, "Failed to connect to database2!");
            return;
        }

        if (!database.existsTable("sc2_clans")) {
            Logging.debug("Creating table: sc2_clans");

            String clanTable = "CREATE TABLE IF NOT EXISTS `sc2_clans` ( `id` INT NOT NULL AUTO_INCREMENT, `tag` VARCHAR(26) NOT NULL, `name` VARCHAR(100) NOT NULL, `verified` TINYINT(1) default 0, `founded` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, `last_action` TIMESTAMP NOT NULL, `allies` TEXT, `rivals` TEXT, `warring` TEXT, `flags` MEDIUMTEXT, PRIMARY KEY (`id`), UNIQUE KEY `id` (`id`), UNIQUE KEY `tag` (`tag`));";


            database.execute(clanTable);
        }

        if (!database.existsTable("sc2_players")) {
            Logging.debug("Creating table: sc2_players");

            String clanTable = "CREATE TABLE IF NOT EXISTS `sc2_players` ( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(16) NOT NULL, `leader` TINYINT(1) default 0, `clan` INT default -1, `rank` INT default -1, `trusted` TINYINT(1) default 0, `banned` TINYINT(1) default 0, `join_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, `last_seen` TIMESTAMP  NOT NULL, `neutral_kills` INT default 0, `rival_kills` INT default 0, `civilian_kills` INT default 0, `deaths` INT default 0, `flags` MEDIUMTEXT, PRIMARY KEY (`id`), UNIQUE KEY `id` (`id`), UNIQUE KEY `name` (`name`) );";

            database.execute(clanTable);
        }

        if (!database.existsTable("sc2_ranks")) {
            Logging.debug("Creating table: sc2_ranks");

            String clanTable = "CREATE TABLE IF NOT EXISTS `sc2_ranks` ( `id` INT NOT NULL AUTO_INCREMENT, `clan` INT NOT NULL, `name` VARCHAR(16) NOT NULL UNIQUE KEY, `tag` VARCHAR(16) NOT NULL UNIQUE KEY,`permissions` MEDIUMTEXT, `priority` INT(3) default -1, PRIMARY KEY (`id`), UNIQUE KEY `id` (`id`) );";

            database.execute(clanTable);
        }


        if (!database.existsTable("sc2_bb")) {
            Logging.debug("Creating table: sc2_bb");

            String clanTable = "CREATE TABLE IF NOT EXISTS `sc2_bb` ( `id` INT NOT NULL AUTO_INCREMENT, `clan` INT NOT NULL, `text` TEXT, `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, UNIQUE KEY `id` (`id`) );";

            database.execute(clanTable);
        }
    }

    public Database getDatabase()
    {
        return database;
    }
}
