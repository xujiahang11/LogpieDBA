package com.logpie.dba.dao;

import com.logpie.dba.support.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Group implements Iterable<DbField> {

    private final List<DbField> dbFields;
    private final Condition having;

    public Group(final Condition having, final SimpleDbField... fields)
    {
        this.dbFields = new ArrayList<>(fields.length);
        for(DbField dbField : fields)
        {
            Assert.notNull(dbField, "DbField must not be null");
            this.dbFields.add(dbField);
        }
        this.having = having;
    }

    public Condition getHaving()
    {
        return having;
    }

    @Override
    public Iterator<DbField> iterator()
    {
        return dbFields.iterator();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Group))
        {
            return false;
        }

        Group that = (Group) obj;
        return this.dbFields.equals(that.dbFields);
    }

    @Override
    public int hashCode()
    {
        int res = 17;
        res = 31 * res + dbFields.hashCode();
        return res;
    }

}
