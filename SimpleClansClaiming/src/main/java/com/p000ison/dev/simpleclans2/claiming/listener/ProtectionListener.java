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
 *     Last modified: 27.02.13 17:12
 */

package com.p000ison.dev.simpleclans2.claiming.listener;

import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.claiming.SimpleClansClaiming;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Represents a ProtectionListener
 */
public class ProtectionListener implements Listener {

    private SimpleClansClaiming plugin;

    public ProtectionListener(SimpleClansClaiming plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ClanPlayer clanPlayer = plugin.getCore().getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.getClan();
        Clan clanHere = plugin.getClaimingManager().getClanAt(player.getLocation());

        if (clan.equals(clanHere)) {
            //allow
        } else {
            //disallow
        }
    }
}
