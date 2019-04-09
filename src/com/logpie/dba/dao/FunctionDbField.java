package com.logpie.dba.dao;

import com.logpie.dba.support.Assert;

import java.util.Locale;

public class FunctionDbField implements DbField {

    private final SimpleDbField simpleField;
    private final String columnAlias;
    private final Function function;

    public FunctionDbField(final Class<?> tableClazz, final String columnLabel, final String columnAlias, final Function function)
    {
        this(tableClazz, null, columnLabel, columnAlias, function);
    }

    /**
     * Constructs a FunctionDbField by default
     *
     * @param tableClazz model class
     * @param tableAlias table alias if existed or null
     * @param columnLabel column name
     * @param function aggregate function
     */
    public FunctionDbField(final Class<?> tableClazz, final String tableAlias, final String columnLabel, final String columnAlias, final Function function)
    {
        this.simpleField = new SimpleDbField(tableClazz, tableAlias, columnLabel);
        Assert.hasLength(columnAlias, "column alias must has length in FunctionDbField");
        this.columnAlias = columnAlias;
        this.function = function;
    }

    @Override
    public Class<?> getTableClazz()
    {
        return simpleField.getTableClazz();
    }

    @Override
    public String getTableAlias()
    {
        return simpleField.getTableAlias();
    }

    @Override
    public String getColumnLabel()
    {
        return simpleField.getColumnLabel();
    }

    public String getColumnAlias()
    {
        return columnAlias;
    }

    /**
     * The default pattern is "Function([table_name_or_alias].[column_label])"
     *
     * @return a specified pattern
     */
    @Override
    public String pattern()
    {
        return function.toString() + "(" + simpleField.pattern() + ")";
    }

    public static enum Function {
        MAX, MIN, AVG, COUNT, SUM;

        public static Function fromString(String value)
        {
            return Function.valueOf(value.toLowerCase(Locale.US));
        }
    }
}
