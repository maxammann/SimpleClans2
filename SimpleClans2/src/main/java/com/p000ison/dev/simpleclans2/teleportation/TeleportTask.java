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

package com.p000ison.dev.simpleclans2.teleportation;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Set;

/**
 * Represents a TeleportTask
 */
public class TeleportTask implements Runnable {

    private SimpleClans plugin;
    private Set<TeleportState> waitingPlayers;

    public TeleportTask(SimpleClans plugin, Set<TeleportState> waitingPlayers)
    {
        this.plugin = plugin;
        this.waitingPlayers = waitingPlayers;
    }

    @Override
    public void run()
    {
        if (waitingPlayers.isEmpty()) {
            return;
        }

        Iterator<TeleportState> players = waitingPlayers.iterator();

        while (players.hasNext()) {
            TeleportState state = players.next();

            if (state.isProcessing()) {
                continue;
            }

            state.setProcessing(true);

            Player player = state.getPlayer();

            if (player == null) {
                players.remove();
                continue;
            }

            boolean isLocationEqual = TeleportManager.isLocationEqual(player.getLocation(), state.getLocation(), plugin.getSettingsManager().getTeleportFuzzyness());

            if (state.isTeleportTime()) {
                if (isLocationEqual) {
                    if (plugin.getSettingsManager().dropItems() && !player.hasPermission("simpleclans.mod.keep-items")) {
                        dropItems(player);
                    }

                    player.teleport(state.getDestination());

                    ChatBlock.sendMessage(player, state.getMessage());
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.moved.teleport.cancelled"));
                }

                players.remove();
            } else if (!isLocationEqual) {
                ChatBlock.sendMessage(player, ChatColor.RED + Language.getTranslation("you.moved.teleport.cancelled"));
                players.remove();
            } else {
                String left;
                if (state.getCounter() == 1) {
                    left = Language.getTranslation("second.left", 1);
                } else {
                    left = Language.getTranslation("seconds.left", state.getCounter());
                }

                ChatBlock.sendMessage(player, ChatColor.AQUA.toString() + left);
            }

            state.setProcessing(false);
        }
    }

    private void dropItems(Player player)
    {

        Inventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();

        for (ItemStack item : contents) {

            if (item == null) {
                continue;
            }

            if (plugin.getSettingsManager().dropItemOnTeleport(item.getType())) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                inv.remove(item);
            }
        }
    }
}
