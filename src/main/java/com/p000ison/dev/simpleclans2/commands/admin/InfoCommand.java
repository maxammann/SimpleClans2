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

package com.p000ison.dev.simpleclans2.commands.admin;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

/**
 * Represents a InfoCommand
 */
public class InfoCommand extends GenericConsoleCommand {

    public InfoCommand(SimpleClans plugin)
    {
        super("Info", plugin);
        setArgumentRange(0, 0);
        setUsages(Language.getTranslation("usage.info", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("info.command"));
        setPermission("simpleclans.admin.info");
    }

    @Override
    public String getMenu()
    {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args)
    {
        int loadedClans = plugin.getClanManager().getClans().size();
        int loadedClanPlayers = plugin.getClanPlayerManager().getClanPlayers().size();
        int dataQueue = plugin.getDataManager().getAutoSaver().size();
        int teleporting = plugin.getTeleportManager().getWaitingPlayers();

        sender.sendMessage("Loaded clans: " + loadedClans);
        sender.sendMessage("Loaded clan players: " + loadedClanPlayers);
        sender.sendMessage("Data in queue: " + dataQueue);
        sender.sendMessage("Teleporting: " + teleporting);
        sender.sendMessage("Requests: " + plugin.getRequestManager().getRequests());

        for (BukkitTask task : plugin.getServer().getScheduler().getPendingTasks()) {
            if (!task.getOwner().equals(plugin)) {
                return;
            }

            sender.sendMessage("-----------------------------------------------------");
            sender.sendMessage("ID: " + task.getTaskId());
            sender.sendMessage("Sync: " + task.isSync());
        }
    }
}
