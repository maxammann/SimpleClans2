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
 *     Last modified: 1/21/13 5:11 PM
 */

package com.p000ison.dev.simpleclans2.api.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Double.doubleToLongBits;

/**
 * A atomic version of a boolean. A AtomicLong is baked.
 */
public final class AtomicDouble implements Serializable {

    private static final long serialVersionUID = 4358799752083982498L;
    private AtomicLong bits;

    public AtomicDouble() {
        this(0f);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(doubleToLongBits(initialValue));
    }

    public final boolean compareAndSet(double expect, double update) {
        return bits.compareAndSet(doubleToLongBits(expect),
                doubleToLongBits(update));
    }

    public final void set(double newValue) {
        bits.set(doubleToLongBits(newValue));
    }

    public final double get() {
        return doubleToLongBits(bits.get());
    }

    public final double getAndSet(double newValue) {
        return doubleToLongBits(bits.getAndSet(doubleToLongBits(newValue)));
    }

    public final void add(double delta) {
        while (true) {
            long current = bits.get();
            long next = current + Double.doubleToLongBits(delta);
            if (compareAndSet(current, next)) {
                break;
            }
        }
    }

    public final void subtract(double delta) {
        add(-delta);
    }

    public final boolean weakCompareAndSet(double expect, double update) {
        return bits.weakCompareAndSet(doubleToLongBits(expect), doubleToLongBits(update));
    }
}

