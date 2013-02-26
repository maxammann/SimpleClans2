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

package com.p000ison.dev.simpleclans2.claiming;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Map;

/**
 * Represents a ChunkCache
 */
public abstract class ChunkCache<V> extends CacheLoader<ChunkLocation, V> {

    private LoadingCache<ChunkLocation, V> cache;

    public ChunkCache(int initial, int maxSize, long duration) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

        builder.maximumSize(maxSize);
//        builder.expireAfterAccess(duration, TimeUnit.SECONDS);
        builder.initialCapacity(initial);
        cache = builder.build(this);
    }

    public void load(Map<ChunkLocation, ? extends V> data) {
        cache.asMap().putAll(data);
    }

    public void load(ChunkLocation location, V data) {
        cache.asMap().put(location, data);
    }

    public V getData(ChunkLocation location) {
//        try {
        return cache.getUnchecked(location);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e.getCause());
//        }
    }

    public void clean() {
        cache.cleanUp();
    }
}
