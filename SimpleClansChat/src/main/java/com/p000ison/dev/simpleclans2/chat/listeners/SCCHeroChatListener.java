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

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Herochat;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Represents a SCCHeroChatListener
 */
@SuppressWarnings("unused")
public class SCCHeroChatListener implements Listener {
    private SimpleClansChat plugin;

    public SCCHeroChatListener(SimpleClansChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onHeroChatMessage(ChannelChatEvent event) {
        if (this.plugin.getSettingsManager().isCompatibilityMode()) {
            String format = event.getFormat();

            if (format.equals("{default}")) {
                format = Herochat.getChannelManager().getStandardFormat();
            }

            event.setFormat(plugin.formatCompatibility(format, event.getSender().getName()));
        }
    }
}
