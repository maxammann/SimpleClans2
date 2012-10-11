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

package com.p000ison.dev.simpleclans2.listeners;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Represents a SCSpoutListener
 */
public class SCSpoutListener {

    private SimpleClans plugin;

    public SCSpoutListener(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSpoutPlayerJoin(SpoutCraftEnableEvent event)
    {
        SpoutPlayer spoutPlayer = event.getPlayer();

        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayerExact(spoutPlayer.getName());

        if (clanPlayer == null) {
            return;
        }

        plugin.getSpoutSupport().updateCape(spoutPlayer, clanPlayer.getClan());
    }
}
