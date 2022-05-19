package org.mabartos.meetmethere.model.caffeine.provider;

import io.quarkus.cache.Cache;

import java.util.function.Function;

public class CacheUtil {

    public static <T, U> T searchCache(Cache cache, U key, Function<U, T> function) {
        return cache.get(key, function).await().indefinitely();
    }
}
