package com.logpie.dba.dao;

public interface DbField extends Parsable {

    public Class<?> getTableClazz();

    public String getTableAlias();

    public String getColumnLabel();

}
