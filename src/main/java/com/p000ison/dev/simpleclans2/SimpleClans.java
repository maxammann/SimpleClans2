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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.commands.AlliancesCommand;
import com.p000ison.dev.simpleclans2.commands.commands.CreateCommand;
import com.p000ison.dev.simpleclans2.commands.commands.ListCommand;
import com.p000ison.dev.simpleclans2.data.DataManager;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.util.Logging;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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
    private DataManager dataManager;
    private static Economy economy;


    @Override
    public void onEnable()
    {
        new Logging(getLogger());
        new Language("en_EN");

        if (!setupEconomy()) {
            Logging.debug(Level.SEVERE, "Economy features disabled due to no Economy dependency found!");
        }

        loadManagers();
    }


    @Override
    public void onDisable()
    {
        Language.clear();

        databaseManager.getDatabase().close();

        Logging.close();
    }

    private boolean setupEconomy()
    {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }


    private void loadManagers()
    {
        settingsManager = new SettingsManager(this);


        databaseManager = new DatabaseManager(this);


        clanManager = new ClanManager(this);
        clanPlayerManager = new ClanPlayerManager(this);
        dataManager = new DataManager(this);
        requestManager = new ClanRequestManager();
        setupCommands();
    }

    private void setupCommands()
    {
        commandManager = new CommandManager(this);

        commandManager.addCommand(new ListCommand(this));
        commandManager.addCommand(new CreateCommand(this));
        commandManager.addCommand(new AlliancesCommand(this));
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

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public static boolean withdrawBalance(String player, double balance)
    {
        return economy.withdrawPlayer(player, balance).transactionSuccess();
    }

    public static void depositBalance(String player, double balance)
    {
        economy.withdrawPlayer(player, balance);
    }

    public static boolean hasEconomy()
    {
        return economy != null;
    }

    public static Economy getEconomy()
    {
        return economy;
    }

}
