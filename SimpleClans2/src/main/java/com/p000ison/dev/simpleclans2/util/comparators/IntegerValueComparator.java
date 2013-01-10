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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

/**
 * Represents a IntegerValueComparator
 */
public class IntegerValueComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = 7694354557639217836L;
    private Map<?, Integer> base;

    public IntegerValueComparator(Map<?, Integer> base)
    {
        this.base = base;
    }

    @Override
    public int compare(String a, String b)
    {
        return base.get(a).compareTo(base.get(b));
    }
}
