package com.logpie.dba.dao;

public class LogicCondition extends Condition {

    private Connector connector;

    public LogicCondition(Connector connector)
    {
        super();
        this.connector = connector;
    }

    public Connector getConnector()
    {
        return connector;
    }

    public enum Connector {
        AND("AND"), OR("OR");

        private String s;

        private Connector(String s)
        {
            this.s = s;
        }

        @Override
        public String toString()
        {
            return this.s;
        }
    }
}
