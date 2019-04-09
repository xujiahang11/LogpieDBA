package com.logpie.dba.core;


import com.logpie.dba.annotation.Column;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

class SimpleRowMapper implements RowMapper<Model> {

    private static final String CLASS_NAME = Model.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);
    private final Class<?> clazz;

    SimpleRowMapper(final Class<?> clazz)
    {
        this.clazz = clazz;
    }

    @Nullable
    @Override
    public Model mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        final Table table = CacheUtil.get(clazz);
        final Model mappedObject = (Model) ReflectionUtil.buildInstanceByDefaultConstructor(clazz);

        if (mappedObject != null)
        {
            for(Field field : table)
            {
                Column column = field.getAnnotation(Column.class);
                Map<String, Table> refTables = table.getReferencedTableMapping();
                if (refTables.containsKey(column.label()))
                {
                    Table refTable = refTables.get(column.label());
                    Model refModel = (Model) ReflectionUtil.buildInstanceByDefaultConstructor(refTable.getTableClass());
                    ReflectionUtil.runSetter(field, mappedObject, refTable.getTableClass(), refModel);
                } else
                {
                    try
                    {
                        Column.DataType dataType = column.type();
                        ReflectionUtil.runSetter(field, mappedObject, dataType.toJavaClass(), rs.getObject(column.label()));
                    } catch (SQLException e)
                    {
                        LOG.warning("SQLException when trying to get object from ResultSet for key: " + column.label());
                    }
                }
            }
        }

        return mappedObject;
    }
}
