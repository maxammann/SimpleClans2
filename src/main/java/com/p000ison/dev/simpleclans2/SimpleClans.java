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


package com.p000ison.dev.simpleclans2;

import com.alta189.simplesave.Database;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.data.DataManager;
import com.p000ison.dev.simpleclans2.data.KillEntry;
import com.p000ison.dev.simpleclans2.data.KillType;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.requests.ClanRequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin {

    private ClanManager clanManager;
    private DatabaseManager databaseManager;
    private ClanPlayerManager clanPlayerManager;
    private SettingsManager settingsManager;
    private ClanRequestManager requestManager;
    private CommandManager commandManager;

    @Override
    public void onEnable()
    {
        new Logging(getLogger());
        new Language();


        loadManagers();
    }


    @Override
    public void onDisable()
    {
        Language.clear();

        try {
            databaseManager.getDatabase().close();
        } catch (ConnectionException e) {
            Logging.debug(e);
        }

        Logging.close();
    }

    private void loadManagers()
    {
        settingsManager = new SettingsManager(this);

        try {
            databaseManager = new DatabaseManager();
        } catch (TableRegistrationException e) {
            Logging.debug(e);
        } catch (ConnectionException e) {
            Logging.debug(e);
        }

        clanManager = new ClanManager(this);
        clanPlayerManager = new ClanPlayerManager(this);
        requestManager = new ClanRequestManager();
        commandManager = new CommandManager(this);
    }

    public static void main(String[] args) throws TableRegistrationException, ConnectionException
    {
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
//
//
//        Set<ClanPlayer> clanPlayerSet = new HashSet<ClanPlayer>();
//
//        for (int i = 0; i < 2000; i++) {
//            ClanPlayer cp = new ClanPlayer(null, String.valueOf(new Random().nextInt()));
//
//            if (i == 1654) {
//               cp.setId(4L);
//            }              else {
//                cp.setId(new Random().nextInt());
//            }
//            clanPlayerSet.add(cp);
//        }
//
//        long start = System.currentTimeMillis();
//
//        for (ClanPlayer cp : clanPlayerSet) {
//            if (cp.getId() == 4) {
//                System.out.println("found");
//                break;
//            }
//        }
//
//        long end = System.currentTimeMillis();
//
//        System.out.println(end - start );
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, java.lang.String label, java.lang.String[] args)
    {
        commandManager.executeAll(null, sender, command.getName(), label, args);
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

    public SettingsManager getSettingsManager()
    {
        return settingsManager;
    }

    public ClanRequestManager getRequestManager()
    {
        return requestManager;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }
}
