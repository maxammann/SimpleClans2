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
 *     Last modified: 31.10.12 21:35
 */


package com.p000ison.dev.simpleclans2.util.comparators;

import com.p000ison.dev.simpleclans2.api.UpdateAble;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a KDRComparator
 */
public class LastSeenComparator implements Comparator<UpdateAble>, Serializable {

    @Override
    public int compare(UpdateAble updateAble1, UpdateAble updateAble2)
    {
        long updated1 = updateAble1.getLastUpdated();
        long updated2 = updateAble2.getLastUpdated();
        return (updated1 < updated2 ? -1 : (updated1 == updated2 ? 0 : 1));
    }
}
