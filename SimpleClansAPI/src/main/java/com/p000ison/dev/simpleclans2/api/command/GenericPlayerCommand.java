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
 *     Last modified: 29.01.13 20:55
 */


package com.p000ison.dev.simpleclans2.api.command;

import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import org.bukkit.entity.Player;

/**
 * Represents a GenericPlayerCommand
 */
public abstract class GenericPlayerCommand extends GenericCommand {


    public GenericPlayerCommand(String name) {
        super(name);
    }

    public abstract void execute(Player player, String[] args);

    /**
     * Gets the menu of this clan. This is unformatted and maybe contains a "{0}" for the base command.
     *
     * @return The menu for this command
     */
    public abstract String getMenu(ClanPlayer clanPlayer);
}
