package com.logpie.dba.dao;

import com.logpie.dba.support.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Selection implements Iterable<DbField> {

    private final boolean isDistinct;
    private final List<DbField> dbFields;

    /**
     * Constructs a Selection with two default values: false & All
     */
    public Selection()
    {
        this(false, All.INSTANCE);
    }

    public Selection(DbField... dbFields)
    {
        this(false, dbFields);
    }

    public Selection(boolean isDistinct) { this(isDistinct, All.INSTANCE); }

    public Selection(boolean isDistinct, DbField... dbFields)
    {
        this.isDistinct = isDistinct;
        this.dbFields = new ArrayList<>(dbFields.length);
        for(DbField dbField : dbFields)
        {
            Assert.notNull(dbField, "DbField must not be null");
            this.dbFields.add(dbField);
        }
    }

    public void add(DbField dbField)
    {
        Assert.notNull(dbField, "DbField must not be null");
        dbFields.add(dbField);
    }

    public boolean isDistinct()
    {
        return isDistinct;
    }

    @Override
    public Iterator<DbField> iterator()
    {
        return dbFields.iterator();
    }
}
