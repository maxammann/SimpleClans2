package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.claiming.commands.ClaimCreateCommand;
import com.p000ison.dev.simpleclans2.claiming.commands.ClaimMapCommand;
import com.p000ison.dev.simpleclans2.claiming.data.ClaimingManager;
import com.p000ison.dev.sqlapi.Database;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.Charset;

/**
 * Represents a SimpleClansClaiming
 */
// Permissions for claiming: build, destroy, interact -> clan permissions, allies, rivals, neutral
public class SimpleClansClaiming extends JavaPlugin {
    private SCCore core;
    private ClaimingManager claimingManager;

    @Override
    public void onEnable() {
        SCClaimingLanguage.setInstance(getDataFolder(), Charset.forName("UTF-8"));
        hookSimpleClans();

        claimingManager = new ClaimingManager(this, getSCDatabase());

        core.getCommandManager().addCommand(new ClaimCreateCommand(core, claimingManager));
        core.getCommandManager().addCommand(new ClaimMapCommand(core, claimingManager));
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

    public ClaimingManager getClaimingManager() {
        return claimingManager;
    }
}
