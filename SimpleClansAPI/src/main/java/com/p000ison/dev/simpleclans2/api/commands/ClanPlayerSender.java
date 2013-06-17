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
 *     Last modified: 31.05.13 12:16
 */

package com.p000ison.dev.simpleclans2.api.commands;

import com.p000ison.dev.commandlib.Command;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import org.bukkit.entity.Player;

/**
 * Represents a ClanPlayerSender
 */
public class ClanPlayerSender implements BukkitSender {

    private final Player sender;
    private final ClanPlayer cp;

    public ClanPlayerSender(Player sender, ClanPlayer cp) {
        this.sender = sender;
        this.cp = cp;
    }

    @Override
    public void sendMessage(String s) {
        sender.sendMessage(s);
    }

    @Override
    public void sendMessage(String s, Object... objects) {
        sender.sendMessage(String.format(s, objects));
    }

    @Override
    public boolean hasPermission(Command command) {
        return command.hasPermission(this);
    }

    @Override
    public boolean hasPermission(String s) {
        return sender.hasPermission(s);
    }

    public ClanPlayer getClanPlayer() {
        return cp;
    }

    @Override
    public Player getSender() {
        return sender;
    }
}
