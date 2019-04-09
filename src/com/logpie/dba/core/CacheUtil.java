package com.logpie.dba.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheUtil {

    private static final String CLASS_NAME = ModelUtils.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);

    private static final Map<Class<?>, Table> cache = new HashMap<>();

    public static Table get(Class<?> c)
    {
        if (cache.containsKey(c))
        {
            return cache.get(c);
        } else
        {
            return generateAndPut(c);
        }

    }

    private static Table generateAndPut(Class<?> c)
    {
        Table table = TableUtils.build(c, null);
        if(table == null)
        {
            LOG.log(Level.WARNING, "Cannot build a Table object from this class: " + c.getName());
        }
        cache.put(c, table);
        return table;
    }

    // TODO: improve the performance of Cache service
    public void LRUCache() {

    }
}
