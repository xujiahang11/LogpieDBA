package com.logpie.dba.dao;

import com.logpie.dba.core.SQLEngine;

import java.util.Collection;
import java.util.Iterator;

public abstract class Clause implements Parsable {

    private final DbField dbField;

    /**
     * Constructs a Clause for the default
     *
     * @param dbField the condition SimpleDbField in this clause
     */
    public Clause(final DbField dbField)
    {
        this.dbField = dbField;
    }
    
    public DbField getDbField()
    {
        return dbField;
    }
    
    public static class Between extends Clause {

        final private Object value1;
        final private Object value2;

        public Between(final DbField dbField, final Object value1, final Object value2)
        {
            super(dbField);
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " BETWEEN " + SQLEngine.toSqlString(value1) + " AND " + SQLEngine.toSqlString(value2);
            return pattern;
        }
    }


    public static class Equal extends Clause {

        final private Object value;

        public Equal(final SimpleDbField simpleField, final Object value)
        {
            super(simpleField);
            this.value = value;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " = " + SQLEngine.toSqlString(value);
            return pattern;
        }
    }


    public static class GreaterThan extends Clause {

        final private Object value;

        public GreaterThan(final SimpleDbField simpleField, final Object value)
        {
            super(simpleField);
            this.value = value;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " > " + SQLEngine.toSqlString(value);
            return pattern;
        }
    }


    public static class LessThan extends Clause {

        final private Object value;

        public LessThan(final SimpleDbField simpleField, final Object value)
        {
            super(simpleField);
            this.value = value;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " < " + SQLEngine.toSqlString(value);
            return pattern;
        }
    }


    public static class Not extends Clause {

        final private Object value;

        public Not(final SimpleDbField simpleField, final Object value)
        {
            super(simpleField);
            this.value = value;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " <> " + SQLEngine.toSqlString(value);
            return pattern;
        }
    }


    public static class IsNull extends Clause {

        public IsNull(final SimpleDbField simpleField)
        {
            super(simpleField);
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " IS NULL";
            return pattern;
        }
    }


    public static class NotNull extends Clause {

        public NotNull(final SimpleDbField simpleField)
        {
            super(simpleField);
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " NOT NULL";
            return pattern;
        }
    }


    public static class In extends Clause {

        final private Collection<?> values;

        public In(final SimpleDbField simpleField, Collection<?> values)
        {
            super(simpleField);
            this.values = values;
        }

        @Override
        public String pattern()
        {
            StringBuilder builder = new StringBuilder();
            String pattern = super.getDbField().pattern();
            builder.append(pattern);
            builder.append(" IN (");

            Iterator i = values.iterator();
            while (i.hasNext())
            {
                builder.append(SQLEngine.toSqlString(i.next()));
                if (i.hasNext())
                {
                    builder.append(", ");
                }
            }
            builder.append(")");

            return builder.toString();
        }
    }


    public static class NotIn extends Clause {

        final private Collection<?> values;

        public NotIn(final SimpleDbField simpleField, Collection<?> values)
        {
            super(simpleField);
            this.values = values;
        }

        @Override
        public String pattern()
        {
            StringBuilder builder = new StringBuilder();
            String pattern = super.getDbField().pattern();
            builder.append(pattern);
            builder.append(" NOT IN (");

            Iterator i = values.iterator();
            while (i.hasNext())
            {
                builder.append(SQLEngine.toSqlString(i.next()));
                if (i.hasNext())
                {
                    builder.append(", ");
                }
            }
            builder.append(")");

            return builder.toString();
        }
    }


    public static class Like extends Clause {

        final private String pattern;

        public Like(final SimpleDbField simpleField, final String pattern)
        {
            super(simpleField);
            this.pattern = pattern;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " LIKE '" + pattern + "'";
            return pattern;
        }
    }

    public static class NotLike extends Clause {

        final private String pattern;

        public NotLike(final SimpleDbField simpleField, final String pattern)
        {
            super(simpleField);
            this.pattern = pattern;
        }

        @Override
        public String pattern()
        {
            String pattern = super.getDbField().pattern();
            pattern += " NOT LIKE '" + pattern + "'";
            return pattern;
        }
    }
}
