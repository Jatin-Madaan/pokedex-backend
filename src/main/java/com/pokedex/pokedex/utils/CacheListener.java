package com.pokedex.pokedex.utils;

import ch.qos.logback.classic.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.LoggerFactory;

public class CacheListener implements CacheEventListener<Object, Object> {

    private Logger logger = (Logger) LoggerFactory.getLogger(CacheListener.class);

    @Override
    public void onEvent(
            CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        logger.info("Key : {}    oldVal: {}  newVal: {}" ,cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }

}
