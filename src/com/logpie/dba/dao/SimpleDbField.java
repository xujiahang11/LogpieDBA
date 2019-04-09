package com.logpie.dba.dao;

public class SimpleDbField implements DbField {

    private final Class<?> tableClazz;
    private final String tableAlias;
    private final String columnLabel;

    public SimpleDbField(final Class<?> tableClazz, final String columnLabel) {
        this(tableClazz, null, columnLabel);
    }

    /**
     * Constructs a SimpleDbField by default
     *
     * @param tableClazz model class
     * @param tableAlias table alias if existed or null
     * @param columnLabel column name
     */
    public SimpleDbField(final Class<?> tableClazz, final String tableAlias, final String columnLabel)
    {
        this.tableClazz = tableClazz;
        this.tableAlias = tableAlias;
        this.columnLabel = columnLabel;
    }

    @Override
    public Class<?> getTableClazz()
    {
        return tableClazz;
    }

    @Override
    public String getTableAlias()
    {
        return tableAlias;
    }

    @Override
    public String getColumnLabel()
    {
        return columnLabel;
    }

    /**
     * The expected pattern is "[table_name_or_alias].[column_label]":
     * "{0}" stands for table name or table columnAlias if existed
     * "{1}" stands for column label
     *
     * @return a specified pattern
     */
    @Override
    public String pattern()
    {
        return "{0}.{1}";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof SimpleDbField))
        {
            return false;
        }

        SimpleDbField that = (SimpleDbField) obj;
        return this.tableClazz.equals(that.tableClazz) && this.columnLabel.equals(that.columnLabel) &&
                       this.tableAlias == null ? that.tableAlias == null : this.tableAlias.equals(that.tableAlias);
    }

    @Override
    public int hashCode()
    {
        int res = 17;
        res = 31 * res + tableClazz.hashCode();
        res = 31 * res + columnLabel.hashCode();
        if(tableAlias != null)
        {
            res = 31 * res + tableAlias.hashCode();
        }
        return res;
    }

}
