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
 *     Last modified: 26.02.13 16:07
 */

package com.p000ison.dev.simpleclans2.claiming.data;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.p000ison.dev.simpleclans2.claiming.ClaimLocation;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Represents a ChunkCache
 */
public abstract class ChunkCache<V> extends CacheLoader<ClaimLocation, V> {

    private LoadingCache<ClaimLocation, V> cache;

    public ChunkCache(int initial, int maxSize, long duration) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

        builder.maximumSize(maxSize);
        builder.expireAfterAccess(duration, TimeUnit.SECONDS);
        builder.initialCapacity(initial);
//        cache = builder.build(this);
    }

    public void load(ClaimLocation location, V data) {
//        cache.put(location, data);
    }

    public V getData(ClaimLocation location) {
        return cache.getUnchecked(location);
    }

    public Map<ClaimLocation, V> getData() {
        return cache.asMap();
    }

    public void clean() {
        cache.cleanUp();
    }
}
