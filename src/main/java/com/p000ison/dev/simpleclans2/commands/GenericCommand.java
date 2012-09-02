/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */

package com.p000ison.dev.simpleclans2.commands;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import org.bukkit.command.CommandSender;

public abstract class GenericCommand implements Command {
    private String name;
    private int minArgs;
    private int maxArgs;
    private String[] identifiers;
    private String[] usage;

    public GenericCommand(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setUsages(String... text)
    {
        this.usage = text;
    }

    @Override
    public void setIdentifiers(String... identifiers)
    {
        this.identifiers = identifiers;
    }

    @Override
    public boolean isIdentifier(String cmd)
    {
        for (String identifier : identifiers) {
            if (cmd.equalsIgnoreCase(identifier)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public abstract String getMenu(ClanPlayer cp, CommandSender sender);

    @Override
    public String[] getUsages()
    {
        return usage;
    }

    @Override
    public int getMaxArguments()
    {
        return maxArgs;
    }

    @Override
    public int getMinArguments()
    {
        return minArgs;
    }

    @Override
    public void setArgumentRange(int min, int max)
    {
        minArgs = min;
        maxArgs = max;
    }
}
