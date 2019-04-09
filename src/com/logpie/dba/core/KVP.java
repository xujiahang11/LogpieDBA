package com.logpie.dba.core;

public class KVP {

    final private String key;
    final private Object value;

    public KVP(final String key, final Object value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public Object getValue()
    {
        return value;
    }

}
