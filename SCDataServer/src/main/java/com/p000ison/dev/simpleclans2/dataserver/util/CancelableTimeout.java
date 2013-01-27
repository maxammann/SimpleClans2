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
 *     Last modified: 26.01.13 21:15
 */

package com.p000ison.dev.simpleclans2.dataserver.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a CancelableTimeout
 */
public class CancelableTimeout {

    private long timeout;
    private AtomicBoolean cancel = new AtomicBoolean(false);

    public CancelableTimeout(long timeout) {

        this.timeout = timeout;
    }

    public boolean await() {
        long last = System.currentTimeMillis();

        while (timeout > 0) {
            long current = System.currentTimeMillis();
            timeout -= (current - last);
            last = current;

            if (cancel.get()) {
                return true;
            }
        }

        return false;
    }

    public void cancel() {
        cancel.set(true);
    }
}
