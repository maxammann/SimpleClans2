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

package com.p000ison.dev.simpleclans2.database.data.response;

import com.p000ison.dev.simpleclans2.SimpleClans;
import org.bukkit.command.CommandSender;

/**
 * Represents a Response
 */
public abstract class Response implements ResponseAble {
    protected final SimpleClans plugin;
    protected final CommandSender sender;

    protected Response(SimpleClans plugin, CommandSender sender)
    {
        this.plugin = plugin;
        this.sender = sender;
    }

    protected CommandSender getRetriever()
    {
        return sender;
    }

    public int[] getBoundings(int completeSize, int page)
    {
        return plugin.getCommandManager().getBoundings(completeSize, page);
    }

    public int[] getBoundings(int page)
    {
        int start = page * plugin.getSettingsManager().getElementsPerPage();
        int end = start + plugin.getSettingsManager().getElementsPerPage();

        return new int[]{start, end};
    }

    public abstract boolean needsRetriever();
}
