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

import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.commandlib.commands.HelpCommand;
import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.commands.ClanPlayerCommand;
import com.p000ison.dev.simpleclans2.api.commands.ClanPlayerSender;
import com.p000ison.dev.simpleclans2.api.commands.ConsoleSender;
import com.p000ison.dev.simpleclans2.api.logging.Logging;
import com.p000ison.dev.simpleclans2.clan.CraftClanManager;
import com.p000ison.dev.simpleclans2.clan.ranks.CraftRankManager;
import com.p000ison.dev.simpleclans2.clanplayer.CraftClanPlayerManager;
import com.p000ison.dev.simpleclans2.commands.admin.*;
import com.p000ison.dev.simpleclans2.commands.clan.*;
import com.p000ison.dev.simpleclans2.commands.clan.bank.BalanceCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bank.DepositCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bank.WithdrawCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBAddCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.BBClearCommand;
import com.p000ison.dev.simpleclans2.commands.clan.bb.ViewBBCommand;
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
import com.p000ison.dev.simpleclans2.requests.CraftRequestManager;
import com.p000ison.dev.simpleclans2.settings.SettingsManager;
import com.p000ison.dev.simpleclans2.support.PreciousStonesSupport;
import com.p000ison.dev.simpleclans2.support.SpoutSupport;
import com.p000ison.dev.simpleclans2.teleportation.TeleportManager;
import com.p000ison.dev.simpleclans2.util.SimpleClansLogger;
import com.p000ison.dev.sqlapi.exception.DatabaseConnectionException;
import com.p000ison.dev.sqlapi.jbdc.JBDCDatabase;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import com.p000ison.dev.sqlapi.sqlite.SQLiteDatabase;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents a SimpleClans
 */
public class SimpleClans extends JavaPlugin implements SCCore {

	private CraftClanManager clanManager;
	private CraftClanPlayerManager clanPlayerManager;
	private SettingsManager settingsManager;
	private CraftRequestManager requestManager;
	private AsyncCommandExecutor commandManager;
	private DatabaseManager dataManager;
	private CraftRankManager rankManager;
	private TeleportManager teleportManager;
	private PreciousStonesSupport preciousStonesSupport;
	private SpoutSupport spoutSupport;
	private ExceptionReporterTask exceptionReporterTask;
	//    private UpdateInformer updater;
	private static Economy economy;
	private HelpCommand helpCommand;

	@Override
	public void onEnable() {
		long startup = System.currentTimeMillis();

		try {
			exceptionReporterTask = new ExceptionReporterTask();
			Logging.setAPILogger(getLogger());
			Logging.setDefaultLogger(new SimpleClansLogger(getLogger(), this));

			//we need to load the settingsManager already here, because we need the data!
			settingsManager = new SettingsManager(this);
			if (!settingsManager.init()) {
				disable();
				return;
			}

			ChatBlock.setHeadColor(getSettingsManager().getHeaderPageColor());
			ChatBlock.setSubColor(getSettingsManager().getSubPageColor());

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

//            if (getSettingsManager().isUpdaterEnabled()) {
//                this.updater = new UpdateInformer(this, "SIMPLECLANS-SIMPLECLANS2", getSettingsManager().getBuildChannel(), getSettingsManager().isLongBuildReport());
//            }

			registerEvents();

			if (getSettingsManager().isReportErrors()) {
				getServer().getScheduler().runTaskTimerAsynchronously(this, exceptionReporterTask, 0L, 1200L);
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
	public void broadcast(String msg) {
		getServer().broadcastMessage(ChatColor.AQUA.toString() + '[' + getSettingsManager().getServerName() + ChatColor.AQUA + ']' + msg);
	}

	public void disable() {
		if (dataManager != null) {
			AutoSaver saver = dataManager.getAutoSaver();
			if (saver != null) {
				saver.run();
			}
		}

		getServer().getPluginManager().disablePlugin(this);
	}

//    /**
//     * Gets if there is a update
//     *
//     * @return Weather there is a update for this plugin.
//     */
//    public boolean isUpdate() {
//        return updater != null && updater.isUpdate();
//    }

//    /**
//     * Updates this plugin
//     *
//     * @return Weather a update was performed or not.
//     */
//    public boolean update() {
//        return jenkins != null && jenkins.update();
//    }

	public void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			Metrics.Graph authGraph = metrics.createGraph("How many servers run in offline mode?");

			if (getServer().getOnlineMode()) {
				authGraph.addPlotter(new Metrics.Plotter() {
					@Override
					public int getValue() {
						return 1;
					}
				});
			} else {
				authGraph.addPlotter(new Metrics.Plotter() {
					@Override
					public int getValue() {
						return 1;
					}
				});
			}
			Metrics.Graph databaseEngines = metrics.createGraph("Database engines");

			if (getDataManager().getDatabase() instanceof MySQLDatabase) {
				databaseEngines.addPlotter(new Metrics.Plotter("MySQL") {
					@Override
					public int getValue() {
						return 1;
					}
				});
			} else if (getDataManager().getDatabase() instanceof SQLiteDatabase) {
				databaseEngines.addPlotter(new Metrics.Plotter("SQLite") {
					@Override
					public int getValue() {
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
	public void onDisable() {
		//save data
		//close the connection to the database
		if (dataManager != null) {
			dataManager.getAutoSaver().run();
			dataManager.close();
		}

		for (Permission perm : getServer().getPluginManager().getPermissions()) {
			if (perm.getName().startsWith("SC")) {
				getServer().getPluginManager().removePermission(perm.getName());
			}
		}

		Language.clear();
		Logging.clear();
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new SCPlayerListener(this), this);
		pm.registerEvents(new SCEntityListener(this), this);
	}

	private boolean setupEconomy() {

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


	private void loadManagers() {
		clanManager = new CraftClanManager(this);
		clanPlayerManager = new CraftClanPlayerManager(this);

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
			return;
		}

		settingsManager.loadPermissions();

		requestManager = new CraftRequestManager(this);
		teleportManager = new TeleportManager(this);
		rankManager = new CraftRankManager(this);
		spoutSupport = new SpoutSupport(this);
		preciousStonesSupport = new PreciousStonesSupport();
		commandManager = new AsyncCommandExecutor() {
			@Override
			public void onPreCommand(CallInformation callInformation) {
				String head = callInformation.getCommand().getName();
				ChatColor headColor = ChatBlock.getHeadingColor();
				ChatColor subColor = ChatBlock.getSubPageColor();

				StringBuilder header = new StringBuilder();
				if (subColor != null) {
					header.append(subColor.toString());
				}

				header.append(head).append(' ');

				if (headColor != null) {
					header.append(headColor.toString());
				}

				ChatBlock.appendLine(header);

				callInformation.reply(header.toString());
				callInformation.reply("");
			}

			@Override
			public void onExecutionBlocked(CommandSender sender, Command command) {
				if (sender instanceof ConsoleSender && command instanceof ClanPlayerCommand) {
					sender.sendMessage(Language.getTranslation("console.can.not"));
				}
			}

			@Override
			public void onPostCommand(CallInformation callInformation) {
			}

			@Override
			public void onDisplayCommandHelp(CommandSender sender, Command command) {
				sender.sendMessage("§cCommand:§e " + command.getName());
				String usage = command.getDescription();

				String sb = "§cUsage:§e " + helpCommand.getHelp(command) + '\n'
						+ "§cDescription:§e " + usage;

				sender.sendMessage(sb);
			}

			@Override
			public void onCommandNotFound(CommandSender sender) {
				sender.sendMessage(Language.getTranslation("command.not.found"));
			}

			@Override
			public void onPermissionFailed(CommandSender sender, Command command) {
			}

			@Override
			public void onPermissionFailed(CommandSender sender) {
				sender.sendMessage(Language.getTranslation("insufficient.permissions"));
			}
		};
		setupCommands();
		getServer().getScheduler().runTaskTimerAsynchronously(this, commandManager, 0L, 5L);
	}

	private void setupCommands() {

		Command command = commandManager.register(new Command() {
			@Override
			public boolean allowExecution(CommandSender sender) {
				return false;
			}
		}.setName("SimpleClans").setDescription("SimpleClans main command").setIdentifiers("clan", "sc"));

		helpCommand = new HelpCommand(commandManager, "Help", "Help", "page", "help");
		command.addSubCommand(helpCommand.addAlias(command));

		command.addSubCommand(new ListCommand(this));
		command.addSubCommand(new CreateCommand(this));
		command.addSubCommand(new ProfileCommand(this));
		command.addSubCommand(new ProfileAnyCommand(this));
		command.addSubCommand(new LookupCommand(this));
		command.addSubCommand(new LookupAnyCommand(this));
		command.addSubCommand(new LeaderboardCommand(this));
		command.addSubCommand(new AlliancesCommand(this));
		command.addSubCommand(new RivalriesCommand(this));
		command.addSubCommand(new RosterCommand(this));
		command.addSubCommand(new RosterAnyCommand(this));
		command.addSubCommand(new VitalsCommand(this));
		command.addSubCommand(new CoordsCommand(this));
		command.addSubCommand(new StatsCommand(this));
		command.addSubCommand(new KillsCommand(this));
		command.addSubCommand(new KillsAnyCommand(this));
		command.addSubCommand(new AllyCommand(this));
		command.addSubCommand(new RivalCommand(this));
		command.addSubCommand(new HomeCommand(this));
		command.addSubCommand(new HomeSetCommand(this));
		command.addSubCommand(new HomeRegroupCommand(this));
		command.addSubCommand(new WarCommand(this));
		command.addSubCommand(new ModifyTagCommand(this));
		command.addSubCommand(new ToggleCommand(this));
		command.addSubCommand(new InviteCommand(this));
		command.addSubCommand(new KickCommand(this));
		command.addSubCommand(new TrustCommand(this));
		command.addSubCommand(new UnTrustCommand(this));
		command.addSubCommand(new PromoteCommand(this));
		command.addSubCommand(new DemoteCommand(this));
		command.addSubCommand(new CapeCommand(this));
		command.addSubCommand(new ClanFFCommand(this));
		command.addSubCommand(new FFCommand(this));
		command.addSubCommand(new ResignCommand(this));
		command.addSubCommand(new VerifyCommand(this));
		command.addSubCommand(new MostKilledCommand(this));
		command.addSubCommand(new VerifyModCommand(this));
		command.addSubCommand(new WarAdminCommand(this));
		command.addSubCommand(new DisbandCommand(this));
		command.addSubCommand(new BanCommand(this));
		command.addSubCommand(new UnbanCommand(this));
		command.addSubCommand(new GlobalFFCommand(this));
		command.addSubCommand(new SaveCommand(this));
		command.addSubCommand(new ReloadCommand(this));
		command.addSubCommand(new InfoCommand(this));
		command.addSubCommand(new ConvertCommand(this));
		command.addSubCommand(new CopyCommand(this));

		command.addSubCommand(new AcceptCommand(this));
		command.addSubCommand(new DenyCommand(this));
		command.addSubCommand(new AbstainCommand(this));


		Command bank = commandManager.register(new Command() {
			@Override
			public boolean allowExecution(CommandSender sender) {
				return false;
			}
		}.setName("Bank").setDescription("Bank management").setIdentifiers("bank"));

		bank.addSubCommand(new DepositCommand(this));
		bank.addSubCommand(new WithdrawCommand(this));
		bank.addSubCommand(new BalanceCommand(this));

		Command bb = commandManager.register(new Command() {
			@Override
			public boolean allowExecution(CommandSender sender) {
				return false;
			}
		}.setName("BB").setDescription("BB management").setIdentifiers("bb"));

		bb.addSubCommand(new ViewBBCommand(this));
		bb.addSubCommand(new BBAddCommand(this));
		bb.addSubCommand(new BBClearCommand(this));

		Command rank = commandManager.register(new Command() {
			@Override
			public boolean allowExecution(CommandSender sender) {
				return false;
			}
		}.setName("Rank").setDescription("Rank management").setIdentifiers("rank"));

		rank.addSubCommand(new RankCreateCommand(this));
		rank.addSubCommand(new RankDeleteCommand(this));
		rank.addSubCommand(new ListRanksCommand(this));
		rank.addSubCommand(new RankDetailCommand(this));
		rank.addSubCommand(new RankAddPermissionCommand(this));
		rank.addSubCommand(new RankRemovePermissionCommand(this));
		rank.addSubCommand(new RankAssignCommand(this));
		rank.addSubCommand(new RankUnAssignCommand(this));
		rank.addSubCommand(new ListPermissionsCommand(this));

		helpCommand.buildHelpCache(ChatColor.GRAY + "§b/-ids-args§f- -usage");
	}

	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, java.lang.String label, java.lang.String[] args) {
		if (commandManager == null) {
			sender.sendMessage("SimpleClans did not load correctly! Please check log!");
			return true;
		}

		CommandSender cmdSender;

		if (sender instanceof Player) {
			ClanPlayer cp = clanPlayerManager.getClanPlayer((Player) sender);
			cmdSender = new ClanPlayerSender((Player) sender, cp);
		} else {
			cmdSender = new ConsoleSender(sender);
		}

		commandManager.executeAll(cmdSender, command.getName(), args);
		return true;
	}

	public JBDCDatabase getClanDatabase() {
		if (dataManager == null) {
			return null;
		}
		return dataManager.getDatabase();
	}

	@Override
	public CraftClanManager getClanManager() {
		return clanManager;
	}

	@Override
	public CraftClanPlayerManager getClanPlayerManager() {
		return clanPlayerManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	@Override
	public CraftRequestManager getRequestManager() {
		return requestManager;
	}

	@Override
	public CommandExecutor getCommandManager() {
		return commandManager;
	}

	@Override
	public Object getSimpleClansDatabase() {
		return getClanDatabase();
	}

	@Override
	public String getTranslation(String key, Object... args) {
		return Language.getTranslation(key, args);
	}

	public DatabaseManager getDataManager() {
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
	public static boolean withdrawBalance(String player, double balance) {
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
	public static boolean depositBalance(String player, double balance) {
		EconomyResponse response = economy.depositPlayer(player, balance);

		if (!response.transactionSuccess()) {
			throw new RuntimeException(response.errorMessage);
		}

		return true;
	}

	public static double getBalance(String player) {
		return economy.getBalance(player);
	}

	public static boolean hasEconomy() {
		return economy != null;
	}

	public static Economy getEconomy() {
		return economy;
	}

	@Override
	public CraftRankManager getRankManager() {
		return rankManager;
	}

	public PreciousStonesSupport getPreciousStonesSupport() {
		return preciousStonesSupport;
	}

	public TeleportManager getTeleportManager() {
		return teleportManager;
	}

	public SpoutSupport getSpoutSupport() {
		return spoutSupport;
	}

	public void serverAnnounce(String message) {
		serverAnnounceRaw(ChatBlock.parseColors(this.getSettingsManager().getDefaultAnnounce().replace("+message", message)));
	}

	public static void serverAnnounceRaw(String message) {
		if (message == null) {
			return;
		}

		Bukkit.broadcastMessage(message);
	}

	public void registerSimpleClansPermission(String name, Map<String, Boolean> permSet) {
		if (getServer().getPluginManager().getPermission(name) != null) {
			getServer().getPluginManager().removePermission(name.toLowerCase());
		}

		getServer().getPluginManager().addPermission(new Permission(name, PermissionDefault.FALSE, permSet));
	}

	public ExceptionReporterTask getExceptionReporter() {
		return exceptionReporterTask;
	}
}
