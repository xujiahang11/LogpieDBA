package com.logpie.dba.dao;

public class Query {

    private final Class<?> c;
    private final Selection selection;
    private final Join join;
    private final Condition condition;

    public Query(final Class<?> c, final Selection selection, final Join join, final Condition condition)
    {
        this.c = c;
        this.selection = selection;
        this.join = join;
        this.condition = condition;
    }

    public static Query getSimpleQuery(final Class<?> c, final Selection selection)
    {
        return new Query(c, selection, Join.INNER, null);
    }

    public Class<?> getC()
    {
        return c;
    }

    public Selection getSelection()
    {
        return selection;
    }

    public Join getJoin()
    {
        return join;
    }

    public Condition getCondition()
    {
        return condition;
    }

}
