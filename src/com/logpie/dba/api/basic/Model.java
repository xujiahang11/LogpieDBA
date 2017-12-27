package com.logpie.dba.api.basic;

import com.logpie.dba.api.annotation.Column;
import com.logpie.dba.api.annotation.ForeignKeyColumn;
import com.logpie.dba.support.ReflectionUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

public class Model implements RowMapper<Model> {

    @Override
    public Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Class clazz = this.getClass();
        final Model mappedObject = (Model)ReflectionUtil.buildInstanceByDefaultConstructor(clazz);
        // Call setter methods.
        if (mappedObject != null) {
            Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
                // Check if the current filed is mapped column filed
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null) {
                    Column annotation = field.getAnnotation(Column.class);
                    Column.DataType dataType = annotation.type();
                    try {
                        ReflectionUtil.runSetter(field, mappedObject, dataType.toJavaClass(), rs.getString(annotation.name()));
                    } catch (SQLException e) {
                        System.out.println("SQLException when trying to get object from ResultSet for key: " + columnAnnotation.name());
                        e.printStackTrace();
                    }
                }
                else {
                    ForeignKeyColumn foreignKeyColumnAnnotation = field.getAnnotation(ForeignKeyColumn.class);
                    if(foreignKeyColumnAnnotation != null) {
                        try {
                            final Class foreignEntityClass = foreignKeyColumnAnnotation.referencedEntityClass();
                            final RowMapper foreignEntityClassRowMapper = (RowMapper) ReflectionUtil.buildInstanceByDefaultConstructor(foreignEntityClass);
                            ReflectionUtil.runSetter(field, mappedObject, foreignEntityClass, foreignEntityClassRowMapper.mapRow(rs, rowNum));
                        } catch (SQLException e) {
                            System.out.println("SQLException when trying to get object from ResultSet for key: " + foreignKeyColumnAnnotation.name());
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return mappedObject;
    }
}
