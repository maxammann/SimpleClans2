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

    public ChunkCache(int initial, int maxSize, long duration)
    {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

        builder.maximumSize(maxSize);
//        builder.expireAfterAccess(duration, TimeUnit.SECONDS);
        builder.initialCapacity(initial);
        cache = builder.build(this);
    }

    public void load(Map<ChunkLocation, ? extends V> data)
    {
        cache.asMap().putAll(data);
    }

    public void load(ChunkLocation location, V data)
    {
        cache.asMap().put(location, data);
    }

    public V getData(ChunkLocation location)
    {
//        try {
            return cache.getUnchecked(location);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e.getCause());
//        }
    }

    public void clean()
    {
        cache.cleanUp();
    }
}
