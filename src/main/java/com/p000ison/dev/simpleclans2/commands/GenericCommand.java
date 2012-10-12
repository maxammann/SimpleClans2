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

package com.p000ison.dev.simpleclans2.commands;

import com.p000ison.dev.simpleclans2.SimpleClans;
import org.bukkit.permissions.Permissible;

import java.util.Arrays;

abstract class GenericCommand implements Command {
    private String name;
    private int minArgs;
    private int maxArgs;
    private String[] identifiers;
    private String[] usage;
    protected SimpleClans plugin;
    private String permission;
    private Type type;

    public GenericCommand(String name, SimpleClans plugin)
    {
        this.name = name;
        this.plugin = plugin;
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
        if (cmd == null) {
            return false;
        }

        for (String identifier : identifiers) {
            if (cmd.equalsIgnoreCase(identifier)) {
                return true;
            }
        }
        return false;
    }

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

    public int[] getBoundings(int completeSize, int page)
    {
        return plugin.getCommandManager().getBoundings(completeSize, page);
    }

    @Override
    public boolean hasPermission(Permissible sender)
    {
        return permission == null || sender.hasPermission(permission);
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof GenericCommand && name.equals(((GenericCommand) obj).getName());
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return "GenericCommand{" +
                "name='" + name + '\'' +
                ", minArgs=" + minArgs +
                ", permission='" + permission + '\'' +
                ", identifiers=" + (identifiers == null ? null : Arrays.asList(identifiers)) +
                ", maxArgs=" + maxArgs +
                '}';
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
