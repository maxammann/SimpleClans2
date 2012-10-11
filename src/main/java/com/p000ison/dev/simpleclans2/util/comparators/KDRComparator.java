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


package com.p000ison.dev.simpleclans2.util.comparators;

import com.p000ison.dev.simpleclans2.KDR;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a KDRComparator
 */
public class KDRComparator implements Comparator<KDR>, Serializable {

    @Override
    public int compare(KDR kdr1, KDR kdr2)
    {
        Float o1 = kdr1.getKDR();
        Float o2 = kdr2.getKDR();

        return o2.compareTo(o1);
    }
}
