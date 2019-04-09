package com.logpie.dba.core;

import java.util.HashMap;
import java.util.Map;

public class CustomizedModel {

    private final Map<String, Object> wrapper;

    public CustomizedModel()
    {
        wrapper = new HashMap<>();
    }

    public Object get(final String columnLabel)
    {
        if (wrapper.containsKey(columnLabel))
        {
            return wrapper.get(columnLabel);
        }
        return null;
    }

    Map<String, Object> getWrapper()
    {
        return wrapper;
    }

    void put(final String columnLabel, final Object value)
    {
        wrapper.put(columnLabel, value);
    }
}
