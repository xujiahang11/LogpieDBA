package com.logpie.dba.dao;

public enum All implements DbField {
    INSTANCE;

    @Override
    public Class<?> getTableClazz()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTableAlias()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnLabel()
    {
        return "*";
    }

    @Override
    public String pattern()
    {
        return getColumnLabel();
    }
}
