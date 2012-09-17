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
 *     Created: 15.09.12 15:13
 */

package com.p000ison.dev.simpleclans2.support;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents a Hook
 */
public class Hook<P extends Plugin> {
    protected P hooked;

    public Hook(String pluginName, Class<?> pluginClass)
    {
        hooked = hook(pluginName, pluginClass);
    }

    @SuppressWarnings("unchecked")
    public final P hook(String pluginName, Class<?> pluginClass)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

        if (plugin == null) {
            return null;
        }

        if (!plugin.getClass().equals(pluginClass)) {
            return null;
        }

        Logging.debug("Hooked %s!", plugin.getName());

        return (P) plugin;
    }

    public boolean isEnabled()
    {
        return hooked != null;
    }
}
