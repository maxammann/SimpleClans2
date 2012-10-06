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

import com.p000ison.dev.simpleclans2.api.Core;
import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clan.ranks.RankManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.admin.*;
import com.p000ison.dev.simpleclans2.commands.clan.*;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBAddCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBClearCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeRegroupCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeSetCommand;
import com.p000ison.dev.simpleclans2.commands.clan.rank.RankAddPermissionCommand;
import com.p000ison.dev.simpleclans2.commands.clan.rank.RankCreateCommand;
import com.p000ison.dev.simpleclans2.commands.clan.rank.RankSetCommand;
import com.p000ison.dev.simpleclans2.commands.general.AlliancesCommand;
import com.p000ison.dev.simpleclans2.commands.general.HelpCommand;
import com.p000ison.dev.simpleclans2.commands.general.LeaderboardCommand;
import com.p000ison.dev.simpleclans2.commands.general.ListCommand;
import com.p000ison.dev.simpleclans2.commands.members.FFCommand;
import com.p000ison.dev.simpleclans2.commands.voting.AbstainCommand;
import com.p000ison.dev.simpleclans2.commands.voting.AcceptCommand;
import com.p000ison.dev.simpleclans2.commands.voting.DenyCommand;
import com.p000ison.dev.simpleclans2.database.Database;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.database.data.DataManager;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.listeners.SCEntityListener;
import com.p000ison.dev.simpleclans2.listeners.SCPlayerListener;
import com.p000ison.dev.simpleclans2.requests.RequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.support.PreciousStonesSupport;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import com.p000ison.dev.simpleclans2.teleportation.TeleportManager;
import com.p000ison.dev.simpleclans2.util.Announcer;
import com.p000ison.dev.simpleclans2.util.Logging;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin implements Core {

    private ClanManager clanManager;
    private DatabaseManager databaseManager;
    private ClanPlayerManager clanPlayerManager;
    private SettingsManager settingsManager;
    private RequestManager requestManager;
    private CommandManager commandManager;
    private DataManager dataManager;
    private RankManager rankManager;
    private TeleportManager teleportManager;
    private PreciousStonesSupport preciousStonesSupport;
    private SpoutSupport spoutSupport;
    private static Economy economy;


    @Override
    public void onEnable()
    {
        long startup = System.currentTimeMillis();

        try {
            Logging.setInstance(getLogger());

            saveResource("languages/lang.properties", true);
            Language.setInstance(new File(getDataFolder(), "languages"), "test");

            if (!setupEconomy()) {
                Logging.debug(Level.SEVERE, "Economy features disabled due to no Economy dependency found!");
            } else {
                Logging.debug("Hooked economy system: %s!", economy.getName());
            }

            Logging.debug("Loading managers...");
            loadManagers();
            Logging.debug("Loading the managers finished!");

            registerEvents();

            Announcer.setPlugin(this);
        } catch (Exception e) {
            Logging.debug(e, "Failed at loading SimpleClans! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        long finish = System.currentTimeMillis();

        Logging.debug(String.format("Enabling took %s ms", finish - startup));
    }


    @Override
    public void onDisable()
    {
        //save data
        dataManager.getQueue().run();
        //close the connection to the database
        databaseManager.getDatabase().close();

        Language.clear();
        Logging.close();
    }

    private void registerEvents()
    {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new SCPlayerListener(this), this);
        pm.registerEvents(new SCEntityListener(this), this);
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
        requestManager = new RequestManager();
        teleportManager = new TeleportManager(this);
        rankManager = new RankManager(this);
        spoutSupport = new SpoutSupport(this);
        preciousStonesSupport = new PreciousStonesSupport();
        commandManager = new CommandManager();
        setupCommands();
    }

    private void setupCommands()
    {
        commandManager.addCommand(new ListCommand(this));
        commandManager.addCommand(new CreateCommand(this));
        commandManager.addCommand(new AlliancesCommand(this));
        commandManager.addCommand(new AllyCommand(this));
        commandManager.addCommand(new CreateCommand(this));
        commandManager.addCommand(new AcceptCommand(this));
        commandManager.addCommand(new DenyCommand(this));
        commandManager.addCommand(new AbstainCommand(this));
        commandManager.addCommand(new RankCreateCommand(this));
        commandManager.addCommand(new BBAddCommand(this));
        commandManager.addCommand(new BBClearCommand(this));
        commandManager.addCommand(new BBCommand(this));
        commandManager.addCommand(new CoordsCommand(this));
        commandManager.addCommand(new DemoteCommand(this));
        commandManager.addCommand(new BanCommand(this));
        commandManager.addCommand(new HelpCommand(this));
        commandManager.addCommand(new HomeCommand(this));
        commandManager.addCommand(new HomeRegroupCommand(this));
        commandManager.addCommand(new HomeSetCommand(this));
        commandManager.addCommand(new GlobalFFCommand(this));
        commandManager.addCommand(new DisbandCommand(this));
        commandManager.addCommand(new LeaderboardCommand(this));
        commandManager.addCommand(new KickCommand(this));
        commandManager.addCommand(new InviteCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
        commandManager.addCommand(new CapeCommand(this));
        commandManager.addCommand(new ResignCommand(this));
        commandManager.addCommand(new ProfileCommand(this));
        commandManager.addCommand(new AnyProfileCommand(this));
        commandManager.addCommand(new VerifyCommand(this));
        commandManager.addCommand(new RankSetCommand(this));
        commandManager.addCommand(new SaveCommand(this));
        commandManager.addCommand(new RankAddPermissionCommand(this));
        commandManager.addCommand(new InfoCommand(this));
        commandManager.addCommand(new FFCommand(this));

//        commandManager.addCommand(new LookupCommand(this));
//        commandManager.addCommand(new RivalriesCommand(this));
//        commandManager.addCommand(new RosterCommand(this));
//        commandManager.addCommand(new VitalsCommand(this));
//        commandManager.addCommand(new StatsCommand(this));
//        commandManager.addCommand(new StrifesCommand(this));
//        commandManager.addCommand(new KillsCommand(this));
//        commandManager.addCommand(new BankCommand(this));
//        commandManager.addCommand(new RivalCommand(this));
//        commandManager.addCommand(new WarCommand(this));
//        commandManager.addCommand(new ModtagCommand(this));
//        commandManager.addCommand(new ToggleCommand(this));
//        commandManager.addCommand(new SetRankCommand(this));
//        commandManager.addCommand(new TrustCommand(this));
//        commandManager.addCommand(new UntrustCommand(this));
//        commandManager.addCommand(new PromoteCommand(this));
//        commandManager.addCommand(new VerifyModCommand(this));
//        commandManager.addCommand(new MostKilledCommand(this));
//        commandManager.addCommand(new WarAdminCommand(this));
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, java.lang.String label, java.lang.String[] args)
    {
        commandManager.execute(sender, command.getName(), args);
        return true;
    }

    public Core getCore()
    {
        return this;
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

    @Override
    public ClanManager getClanManager()
    {
        return clanManager;
    }

    @Override
    public ClanPlayerManager getClanPlayerManager()
    {
        return clanPlayerManager;
    }

    @Override
    public SettingsManager getSettingsManager()
    {
        return settingsManager;
    }

    @Override
    public RequestManager getRequestManager()
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

    @Override
    public RankManager getRankManager()
    {
        return rankManager;
    }

    public PreciousStonesSupport getPreciousStonesSupport()
    {
        return preciousStonesSupport;
    }

    @Override
    public TeleportManager getTeleportManager()
    {
        return teleportManager;
    }

    public SpoutSupport getSpoutSupport()
    {
        return spoutSupport;
    }
}
