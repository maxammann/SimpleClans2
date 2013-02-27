package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.sqlapi.Database;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a SimpleClansClaiming
 */
public class SimpleClansClaiming extends JavaPlugin {
    private SCCore core;
    private DatabaseManager databaseManager;

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
}
