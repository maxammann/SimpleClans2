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
 *     Created: 09.10.12 18:25
 */

package com.p000ison.dev.simpleclans2.database.data.response;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.util.Logging;

import java.util.LinkedList;

/**
 * Represents a ResponseTask
 */
public class ResponseTask extends LinkedList<Response> implements Runnable {

    private SimpleClans plugin;

    public ResponseTask(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        Response response;

        while ((response = this.poll()) != null) {
            if (response.getRetriever() != null && !response.execute()) {
                Logging.debug("Failed to execute query!");
            }
        }
    }
}