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
 *     Last modified: 05.01.13 15:18
 */


package com.p000ison.dev.simpleclans2;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.clan.ClanManager;
import com.p000ison.dev.simpleclans2.clan.ranks.RankManager;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.Command;
import com.p000ison.dev.simpleclans2.commands.CommandManager;
import com.p000ison.dev.simpleclans2.commands.admin.*;
import com.p000ison.dev.simpleclans2.commands.clan.*;
import com.p000ison.dev.simpleclans2.commands.clan.bank.BalanceCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bank.DepositCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bank.WithdrawCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBAddCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBClearCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeRegroupCommand;
import com.p000ison.dev.simpleclans2.commands.clan.home.HomeSetCommand;
import com.p000ison.dev.simpleclans2.commands.clan.rank.*;
import com.p000ison.dev.simpleclans2.commands.general.*;
import com.p000ison.dev.simpleclans2.commands.members.FFCommand;
import com.p000ison.dev.simpleclans2.commands.members.StatsCommand;
import com.p000ison.dev.simpleclans2.commands.voting.AbstainCommand;
import com.p000ison.dev.simpleclans2.commands.voting.AcceptCommand;
import com.p000ison.dev.simpleclans2.commands.voting.DenyCommand;
import com.p000ison.dev.simpleclans2.database.AutoSaver;
import com.p000ison.dev.simpleclans2.database.DatabaseManager;
import com.p000ison.dev.simpleclans2.exceptions.handling.ExceptionReporterTask;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.listeners.SCEntityListener;
import com.p000ison.dev.simpleclans2.listeners.SCPlayerListener;
import com.p000ison.dev.simpleclans2.metrics.OfflinePlotter;
import com.p000ison.dev.simpleclans2.metrics.OnlinePlotter;
import com.p000ison.dev.simpleclans2.requests.RequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.support.PreciousStonesSupport;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import com.p000ison.dev.simpleclans2.teleportation.TeleportManager;
import com.p000ison.dev.simpleclans2.updater.AutoUpdater;
import com.p000ison.dev.simpleclans2.util.Logging;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.sqlapi.exception.DatabaseConnectionException;
import com.p000ison.dev.sqlapi.jbdc.JBDCDatabase;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import com.p000ison.dev.sqlapi.sqlite.SQLiteDatabase;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin implements SCCore {

    private ClanManager clanManager;
    private ClanPlayerManager clanPlayerManager;
    private SettingsManager settingsManager;
    private RequestManager requestManager;
    private CommandManager commandManager;
    private DatabaseManager dataManager;
    private RankManager rankManager;
    private TeleportManager teleportManager;
    private PreciousStonesSupport preciousStonesSupport;
    private SpoutSupport spoutSupport;
    private ExceptionReporterTask exceptionReporterTask;
    private AutoUpdater updater;
    private static Economy economy;

    @Override
    public void onEnable()
    {
        long startup = System.currentTimeMillis();

        try {
            exceptionReporterTask = new ExceptionReporterTask();
            Logging.setInstance(getLogger(), this);

            //we need to load the settingsManager already here, because we need the data!
            settingsManager = new SettingsManager(this);
            if (!settingsManager.init()) {
                disable();
                return;
            }

            Logging.debug("Loading the language file..");
            long startLanguage = System.currentTimeMillis();
            Language.setInstance(new File(getDataFolder(), "languages"), settingsManager.getCharset());
            Logging.debug("Loading the language file finished! Took %s ms!", System.currentTimeMillis() - startLanguage);

            if (!setupEconomy()) {
                Logging.debug(Level.SEVERE, "Economy features disabled due to no Economy dependency found!");
            } else {
                Logging.debug("Hooked economy system: %s!", economy.getName());
            }

            loadManagers();

            ChatBlock.setHeadColor(getSettingsManager().getHeaderPageColor());
            ChatBlock.setSubColor(getSettingsManager().getSubPageColor());

            if (getSettingsManager().isUpdaterEnabled()) {
                this.updater = new AutoUpdater(this, getSettingsManager().getBuildChannel(), getSettingsManager().isLongBuildReport());
            }

            registerEvents();

            if (getSettingsManager().isReportErrors()) {
                getServer().getScheduler().scheduleAsyncRepeatingTask(this, exceptionReporterTask, 0L, 1200L);
            } else {
                exceptionReporterTask = null;
            }

            getClanPlayerManager().updateOnlinePlayers();

            setupMetrics();

        } catch (RuntimeException e) {
            Logging.debug(e, "Failed at loading SimpleClans! Disabling...", true);
            if (exceptionReporterTask != null) {
                exceptionReporterTask.run();
            }
            disable();
            return;
        }

        long finish = System.currentTimeMillis();

        Logging.debug(String.format("Enabling took %s ms", finish - startup));
    }

    //    private void enableDebuggingConsole()
//    {
//        Interpreter beanshell = new Interpreter();
//        try {
//            beanshell.set("plugin", this);
//            beanshell.set("portnum", 1234);
//            beanshell.eval("setAccessibility(true)");
//            beanshell.eval("server(portnum)");
//        } catch (Exception e) {
//            Logging.debug(e, true);
//        }
//    }
    public void broadcast(String msg)
    {
        getServer().broadcastMessage(ChatColor.AQUA.toString() + '[' + getSettingsManager().getServerName() + ChatColor.AQUA + ']' + msg);
    }

    public void disable()
    {
        if (dataManager != null) {
            AutoSaver saver = dataManager.getAutoSaver();
            if (saver != null) {
                saver.run();
            }
        }

        economy = null;

        getServer().getPluginManager().disablePlugin(this);
    }

    /**
     * Gets if there is a update
     *
     * @return Weather there is a update for this plugin.
     */
    public boolean isUpdate()
    {
        return updater != null && updater.isUpdate();
    }

    /**
     * Updates this plugin
     *
     * @return Weather a update was performed or not.
     */
    public boolean update()
    {
        return updater != null && updater.update();
    }

    public void setupMetrics()
    {
        try {
            Metrics metrics = new Metrics(this);
            Metrics.Graph authGraph = metrics.createGraph("How many servers run in offline mode?");

            if (getServer().getOnlineMode()) {
                authGraph.addPlotter(new OnlinePlotter());
            } else {
                authGraph.addPlotter(new OfflinePlotter());
            }
            Metrics.Graph databaseEngines = metrics.createGraph("Database engines");

            if (getDataManager().getDatabase() instanceof MySQLDatabase) {
                databaseEngines.addPlotter(new Metrics.Plotter("MySQL") {
                    @Override
                    public int getValue()
                    {
                        return 1;
                    }
                });
            } else if (getDataManager().getDatabase() instanceof SQLiteDatabase) {
                databaseEngines.addPlotter(new Metrics.Plotter("SQLite") {
                    @Override
                    public int getValue()
                       {
                            return 1;
                    }
                });
            }

            metrics.start();
        } catch (IOException e) {
            Logging.debug(e, true);
        }
    }

    @Override
    public void onDisable()
    {
        //save data
        //close the connection to the database
        if (dataManager != null) {
            dataManager.getAutoSaver().run();
            dataManager.close();
        }

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
        clanManager = new ClanManager(this);
        clanPlayerManager = new ClanPlayerManager(this);
        settingsManager.loadPermissions();

        try {
            dataManager = new DatabaseManager(this);
        } catch (DatabaseConnectionException e) {
            Logging.debug("----------------------------------------------");
            Logging.debug(Level.SEVERE, "The connection to the database failed: %s!", e.getMessage());
            if (e.getConfig() instanceof MySQLConfiguration) {
                Logging.debug(Level.SEVERE, "If you do not have a MySQL database you can use a SQLite database!");
            }
            Logging.debug("----------------------------------------------");
            disable();
        }

        requestManager = new RequestManager(this);
        teleportManager = new TeleportManager(this);
        rankManager = new RankManager(this);
        spoutSupport = new SpoutSupport(this);
        preciousStonesSupport = new PreciousStonesSupport();
        commandManager = new CommandManager(this);
        setupCommands();
    }

    private void setupCommands()
    {
        commandManager.addCommand(new ListCommand(this));
        commandManager.addCommand(new CreateCommand(this));
        commandManager.addCommand(new ProfileCommand(this));
        commandManager.addCommand(new ProfileAnyCommand(this));
        commandManager.addCommand(new LookupCommand(this));
        commandManager.addCommand(new LookupAnyCommand(this));
        commandManager.addCommand(new LeaderboardCommand(this));
        commandManager.addCommand(new AlliancesCommand(this));
        commandManager.addCommand(new RivalriesCommand(this));
        commandManager.addCommand(new RosterCommand(this));
        commandManager.addCommand(new RosterAnyCommand(this));
        commandManager.addCommand(new VitalsCommand(this));
        commandManager.addCommand(new CoordsCommand(this));
        commandManager.addCommand(new StatsCommand(this));
        commandManager.addCommand(new KillsCommand(this));
        commandManager.addCommand(new KillsAnyCommand(this));
        commandManager.addCommand(new AllyCommand(this));
        commandManager.addCommand(new RivalCommand(this));
        commandManager.addCommand(new HomeCommand(this));
        commandManager.addCommand(new HomeSetCommand(this));
        commandManager.addCommand(new HomeRegroupCommand(this));
        commandManager.addCommand(new WarCommand(this));
        commandManager.addCommand(new ModifyTagCommand(this));
        commandManager.addCommand(new ToggleCommand(this));
        commandManager.addCommand(new InviteCommand(this));
        commandManager.addCommand(new KickCommand(this));
        commandManager.addCommand(new TrustCommand(this));
        commandManager.addCommand(new UnTrustCommand(this));
        commandManager.addCommand(new PromoteCommand(this));
        commandManager.addCommand(new DemoteCommand(this));
        commandManager.addCommand(new CapeCommand(this));
        commandManager.addCommand(new ClanFFCommand(this));
        commandManager.addCommand(new FFCommand(this));
        commandManager.addCommand(new ResignCommand(this));
        commandManager.addCommand(new VerifyCommand(this));
        commandManager.addCommand(new MostKilledCommand(this));
        commandManager.addCommand(new VerifyModCommand(this));
        commandManager.addCommand(new WarAdminCommand(this));
        commandManager.addCommand(new DisbandCommand(this));
        commandManager.addCommand(new BanCommand(this));
        commandManager.addCommand(new UnbanCommand(this));
        commandManager.addCommand(new GlobalFFCommand(this));
        commandManager.addCommand(new SaveCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
        commandManager.addCommand(new InfoCommand(this));
        commandManager.addCommand(new UpgradeCommand(this));
        commandManager.addCommand(new ConvertCommand(this));
        commandManager.addCommand(new CopyCommand(this));

        commandManager.addCommand(new AcceptCommand(this));
        commandManager.addCommand(new DenyCommand(this));
        commandManager.addCommand(new AbstainCommand(this));

        commandManager.addCommand(new DepositCommand(this));
        commandManager.addCommand(new WithdrawCommand(this));
        commandManager.addCommand(new BalanceCommand(this));

        commandManager.addCommand(new BBCommand(this));
        commandManager.addCommand(new BBAddCommand(this));
        commandManager.addCommand(new BBClearCommand(this));

        commandManager.addCommand(new RankCreateCommand(this));
        commandManager.addCommand(new RankDeleteCommand(this));
        commandManager.addCommand(new ListRanksCommand(this));
        commandManager.addCommand(new RankDetailCommand(this));
        commandManager.addCommand(new RankAddPermissionCommand(this));
        commandManager.addCommand(new RankRemovePermissionCommand(this));
        commandManager.addCommand(new RankAssignCommand(this));
        commandManager.addCommand(new RankUnAssignCommand(this));
        commandManager.addCommand(new ListPermissionsCommand(this));

//        commandManager.addCommand(new StrifesCommand(this));
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, java.lang.String label, java.lang.String[] args)
    {
        Command.Type type = Command.Type.getByCommand(command.getName());
        if (type != null) {
            commandManager.execute(sender, command.getName(), type, args);
        }
        return true;
    }

    public SCCore getCore()
    {
        return this;
    }

    @Override
    public JBDCDatabase getSimpleClansDatabase()
    {
        if (dataManager == null) {
            return null;
        }
        return dataManager.getDatabase();
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

    public DatabaseManager getDataManager()
    {
        return dataManager;
    }

    /**
     * Withdraws money from a player's account using Vault. This should work else it will throw a RuntimeException. So be sure that this will not get thrown.
     *
     * @param player  The name of the player
     * @param balance The balance to withdraw
     * @return Weather it was successfully.
     * @throws RuntimeException If something was wrong
     */
    public static boolean withdrawBalance(String player, double balance)
    {
        EconomyResponse response = economy.withdrawPlayer(player, balance);

        if (!response.transactionSuccess()) {
            throw new RuntimeException(response.errorMessage);
        }
        return true;
    }

    /**
     * Deposits money to a player's account using Vault. This should work else it will throw a RuntimeException. So be sure that this will not get thrown.
     *
     * @param player  The name of the player
     * @param balance The balance to deposit
     * @return Weather it was successfully.
     * @throws RuntimeException If something was wrong
     */
    public static boolean depositBalance(String player, double balance)
    {
        EconomyResponse response = economy.depositPlayer(player, balance);

        if (!response.transactionSuccess()) {
            throw new RuntimeException(response.errorMessage);
        }

        return true;
    }

    public static double getBalance(String player)
    {
        return economy.getBalance(player);
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

    public void serverAnnounce(String message)
    {
        serverAnnounceRaw(ChatBlock.parseColors(this.getSettingsManager().getDefaultAnnounce().replace("+message", message)));
    }

    public static void serverAnnounceRaw(String message)
    {
        if (message == null) {
            return;
        }

        Bukkit.broadcastMessage(message);
    }

    public ExceptionReporterTask getExceptionReporter()
    {
        return exceptionReporterTask;
    }
}
