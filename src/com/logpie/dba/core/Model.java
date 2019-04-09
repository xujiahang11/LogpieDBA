package com.logpie.dba.core;

import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.ReferencedColumn;
import com.logpie.dba.support.Assert;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Model implements RowMapper<Model> {

    private static final String CLASS_NAME = Model.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);

    @Nullable
    @Override
    public Model mapRow(ResultSet rs, int rowNum)
    {
        final Class clazz = this.getClass();
        final Table table = CacheUtil.get(clazz);
        final Model mappedObject = (Model) ReflectionUtil.buildInstanceByDefaultConstructor(clazz);

        if (mappedObject != null)
        {
            for(Field field : table)
            {
                Column column = field.getAnnotation(Column.class);
                Map<String, Table> mapping = table.getReferencedTableMapping();
                if (field.isAnnotationPresent(ReferencedColumn.class))
                {
                    Table refTable = mapping.get(column.label());
                    Model refRowMapper = (Model) ReflectionUtil.buildInstanceByDefaultConstructor(refTable.getTableClass());
                    Assert.notNull(refRowMapper, "Cannot get RowMapper of table: " + refTable.getName());

                    ReflectionUtil.runSetter(field, mappedObject, refTable.getTableClass(), refRowMapper.mapRow(rs, rowNum));
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
