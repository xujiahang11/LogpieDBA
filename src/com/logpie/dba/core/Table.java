package com.logpie.dba.core;

import com.logpie.dba.annotation.Column;
import com.logpie.dba.dao.DbField;
import com.logpie.dba.support.Assert;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Table implements Iterable<Field> {

    private final Class<?> tableClass;
    private final String name;
    private String alias;
    private final Field idField;
    private final List<Field> columnFields;
    private final Map<String, Table> referencedTableMapping;

    /**
     * Constructs a Table by default
     *
     * @param tableClass model class
     * @param name table name
     * @param alias table alias and could be set after construction
     * @param idField primary key field which has @ID and @Column annotation
     * @param columnFields a List of column field which has @Column annotation
     * @param referencedTableMapping a Map which maps a foreign key to the referenced table
     */
    Table(final Class<?> tableClass, final String name, final String alias, final Field idField,
          final List<Field> columnFields, final Map<String, Table> referencedTableMapping)
    {
        this.tableClass = tableClass;
        this.name = name;
        this.alias = alias;
        this.idField = idField;
        this.columnFields = columnFields;
        this.referencedTableMapping = referencedTableMapping;
    }

    Class<?> getTableClass()
    {
        return tableClass;
    }

    /**
     * @return original table name
     */
    String getName()
    {
        return name;
    }

    /**
     * @return table alias if existed, or return null
     */
    String getAlias()
    {
        return alias;
    }

    /**
     *
     * @param alias table alias
     */
    void setAlias(String alias) { this.alias = alias; }

    /**
     * @return table alias if existed, or return table original name
     */
    String getNameOrAliasIfExisted()
    {
        if (alias == null || alias.equals(""))
        {
            return getName();
        }
        return alias;
    }

    public Field getIdField()
    {
        return idField;
    }

    Map<String, Table> getReferencedTableMapping()
    {
        return referencedTableMapping;
    }

    public String getIdLabel()
    {
        Assert.notNull(idField, "@ID field must not be null");
        Column id = idField.getAnnotation(Column.class);
        return id.label();
    }

    /**
     * Find if this table has a matched key compared with the given field.
     *
     * The comparison rule is:
     * 1. Is this table the same Java Class with the target one?
     * 2. Do they have the same table name?
     * 3. Do they have the same table alias?
     * 4. Does this table have a key whose label is the same with the given field's label?
     *
     * @param field a table field
     * @return
     */
    boolean hasColumnEquals(DbField field)
    {
        if(field == null)
        {
            return false;
        }

        Class<?> c = field.getTableClazz();
        if(tableClass != c)
        {
            return false;
        }

        String tableAlias = field.getTableAlias();
        String columnLabel = field.getColumnLabel();
        if(tableAlias == null)  // Check auto-generated alias
        {
            String tableName = TableUtils.getTableName(c);
            Assert.notNull(tableName, "Table name must not be null");
            if(alias == null || alias.endsWith(tableName))
            {
                return hasColumnEquals(columnLabel);
            }
        }else if(tableAlias.equals(alias))  // Check customized alias
        {
            return hasColumnEquals(columnLabel);
        }

        return false;
    }

    private boolean hasColumnEquals(String columnLabel)
    {
        for(Field field : columnFields)
        {
            Column column = field.getAnnotation(Column.class);
            if(columnLabel.equals(column.label())) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (o instanceof Table)
        {
            Table table = (Table) o;
            if (table.getAlias() == null || table.getAlias().isEmpty())
            {
                return table.getTableClass() == this.tableClass;
            }
            return table.getAlias().equals(this.alias);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        if (getAlias() == null || getAlias().isEmpty())
        {
            return getTableClass().hashCode();
        }
        return getAlias().hashCode();
    }

    @Override
    public Iterator<Field> iterator()
    {
        return columnFields.iterator();
    }

}
