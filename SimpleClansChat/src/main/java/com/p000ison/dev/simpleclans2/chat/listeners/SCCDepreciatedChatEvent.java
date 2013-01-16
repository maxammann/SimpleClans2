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
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Represents a SCCDepreciatedChatEvent
 */
@SuppressWarnings("deprecation")
public class SCCDepreciatedChatEvent implements Listener {

    private SCCPlayerListener listener;

    public SCCDepreciatedChatEvent(SCCPlayerListener listener) {
        this.listener = listener;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerChatEvent event) {
        AsyncPlayerChatEvent async = listener.onPlayerChat(new AsyncPlayerChatEvent(false, event.getPlayer(), event.getFormat(), event.getRecipients()));
        event.setFormat(async.getFormat());
        event.setCancelled(async.isCancelled());
    }
}
