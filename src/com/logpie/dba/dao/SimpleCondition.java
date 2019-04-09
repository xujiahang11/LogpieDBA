package com.logpie.dba.dao;

public class SimpleCondition extends Condition {

    private Clause clause;

    public SimpleCondition(Clause clause)
    {
        super();
        this.clause = clause;
    }

    public Clause getClause()
    {
        return clause;
    }
}
