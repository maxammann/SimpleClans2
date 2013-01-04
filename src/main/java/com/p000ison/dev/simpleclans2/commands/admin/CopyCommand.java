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
 *     Last modified: 04.01.13 15:31
 */

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clan.ranks.Rank;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.database.BBTable;
import com.p000ison.dev.simpleclans2.database.statements.KillStatement;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.sqlapi.*;
import com.p000ison.dev.sqlapi.exception.DatabaseConnectionException;
import com.p000ison.dev.sqlapi.jbdc.JBDCDatabase;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import com.p000ison.dev.sqlapi.query.PreparedQuery;
import com.p000ison.dev.sqlapi.query.PreparedSelectQuery;
import com.p000ison.dev.sqlapi.sqlite.SQLiteConfiguration;
import com.p000ison.dev.sqlapi.sqlite.SQLiteDatabase;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.text.MessageFormat;


public class CopyCommand extends GenericConsoleCommand {

    public CopyCommand(SimpleClans plugin)
    {
        super("Copy", plugin);
        setArgumentRange(2, 5);
        setUsages(MessageFormat.format(Language.getTranslation("usage.copy"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("copy.command"));
        setPermission("simpleclans.admin.copy");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.copy"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        long start = System.currentTimeMillis();

        Database from = plugin.getSimpleClansDatabase();
        JBDCDatabase to = null;
        DatabaseConfiguration config = null;

        String action = args[0];

        try {
            if (action.equalsIgnoreCase("mysql")) {
                String[] address = args[1].split(":");
                int port = 3306;
                if (address.length == 2) {
                    try {
                        port = Integer.parseInt(address[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Language.getTranslation("number.format"));
                        return;
                    }
                }
                config = new MySQLConfiguration(args[3], args[4], address[0], port, args[2]);

                to = (JBDCDatabase) DatabaseManager.getConnection(config);
                if (to == null) {
                    to = new MySQLDatabase(config);
                }
            } else if (action.equalsIgnoreCase("sqlite")) {
                File file = new File(args[1]);
                config = new SQLiteConfiguration(file);
                to = (JBDCDatabase) DatabaseManager.getConnection(config);
                if (to == null) {
                    to = new SQLiteDatabase(config);
                }
            }

            if (config == null) {
                sender.sendMessage("Database type not supported!");
                return;
            }

            sender.sendMessage("Starting converting!");
            copyFixed(Clan.class, from, to);
            copyFixed(ClanPlayer.class, from, to);
            copyFixed(Rank.class, from, to);
            copyFixed(BBTable.class, from, to);
            copyFixed(KillStatement.class, from, to);
            to.close();
            sender.sendMessage("Successfully converted!");
        } catch (DatabaseConnectionException e) {
            Logging.debug(e, true);
            sender.sendMessage("Converting failed!");
        }

        long end = System.currentTimeMillis();
        ChatBlock.sendMessage(sender, ChatColor.AQUA + MessageFormat.format(Language.getTranslation("data.copied"), end - start));
    }

    private static <T extends TableObject> void copyFixed(Class<T> table, Database from, Database to)
    {
        RegisteredTable registeredTable = to.getRegisteredTable(table);
        if (registeredTable == null) {
            registeredTable = to.registerTable(table);
        }

        PreparedSelectQuery<T> prepare = from.<T>select().from(table).prepare();

        PreparedQuery statement = createFullInsertStatement(to, registeredTable, false);
        PreparedQuery statementWithId = createFullInsertStatement(to, registeredTable, true);

        for (T entry : prepare.getResults()) {
            Object value = registeredTable.getIDColumn().getValue(entry);
            boolean isNull = value == null || value.equals(0);

            if (isNull && to.existsEntry(registeredTable, entry)) {
                to.update(entry);
            } else {
                int i = 0;
                for (Column column : registeredTable.getRegisteredColumns()) {
                    if (value == null) {
                        statementWithId.set(column, i, column.getValue(entry));
                    } else {
                        if (column.equals(registeredTable.getIDColumn())) {
                            continue;
                        }
                        statement.set(column, i, column.getValue(entry));
                    }
                    i++;
                }
            }

            if (isNull) {
                statementWithId.update();
            } else {
                statement.update();
            }
        }
        statement.close();
        statementWithId.close();
        prepare.close();
    }

    private static PreparedQuery createFullInsertStatement(Database database, RegisteredTable table, boolean id)
    {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(table.getName()).append(" (");

        Column idColumn = table.getIDColumn();

        for (Column column : table.getRegisteredColumns()) {
            if (!id) {
                if (column.equals(idColumn)) {
                    continue;
                }
            }
            query.append(column.getName()).append(',');
        }

        query.deleteCharAt(query.length() - 1);
        query.append(") VALUES (");
        for (int i = 0; i < table.getRegisteredColumns().size() - (id ? 0 : 1); i++) {
            query.append("?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(");");

        return database.createPreparedStatement(query.toString());
    }
}
