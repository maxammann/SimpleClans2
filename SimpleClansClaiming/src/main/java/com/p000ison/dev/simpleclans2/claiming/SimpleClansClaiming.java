package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clan.ClanManager;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;
import com.p000ison.dev.simpleclans2.claiming.data.DatabaseManager;
import com.p000ison.dev.sqlapi.Database;
import com.p000ison.dev.sqlapi.mysql.MySQLConfiguration;
import com.p000ison.dev.sqlapi.mysql.MySQLDatabase;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a SimpleClansClaiming
 */
// Permissions for claiming: build, destroy, interact -> clan permissions, allies, rivals, neutral
public class SimpleClansClaiming extends JavaPlugin {
    private SCCore core;
    private DatabaseManager databaseManager;
    private ClaimingManager claimingManager;


    @Override
    public void onEnable() {
        hookSimpleClans();

        databaseManager = new DatabaseManager(getSCDatabase());
    }

    private boolean hookSimpleClans() {
        try {
            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
                if (plugin instanceof SCCore) {
                    this.core = (SCCore) plugin;
                    return true;
                }
            }
        } catch (NoClassDefFoundError e) {
            return false;
        }

        return false;
    }

    public SCCore getCore() {
        return core;
    }

    Database getSCDatabase() {
        if (!(core.getSimpleClansDatabase() instanceof Database)) {
            throw new IllegalArgumentException("The database api of your simpleclans implementation is not supported!");
        }
        return (Database) core.getSimpleClansDatabase();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }


    public ClanPlayerManager getClanPlayerManager() {
        return this.core.getClanPlayerManager();
    }

    public ClanManager getClanManager() {
        return this.core.getClanManager();
    }

    public static void main(String[] args) {
        Database database = new MySQLDatabase(new MySQLConfiguration("root", "m1nt", "localhost", 3306, "sc"));

        DatabaseManager db = new DatabaseManager(database);

        System.out.println(db.getStoredClaim(new ClaimLocation(0, 0, (short)0)).getClanID());
    }

    public ClaimingManager getClaimingManager() {
        return claimingManager;
    }
}
