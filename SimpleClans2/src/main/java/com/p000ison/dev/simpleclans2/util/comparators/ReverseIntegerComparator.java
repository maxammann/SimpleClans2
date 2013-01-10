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
 *     Last modified: 01.11.12 19:59
 */

package com.p000ison.dev.simpleclans2.util.comparators;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a ReverseIntegerComparator
 */
public class ReverseIntegerComparator implements Comparator<Integer>, Serializable {

    private static final long serialVersionUID = 1142321453403266083L;

    @Override
    public int compare(Integer a, Integer b)
    {
        return b.compareTo(a);
    }
}
