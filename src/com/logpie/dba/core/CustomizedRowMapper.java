package com.logpie.dba.core;

import com.logpie.dba.dao.DbField;
import com.logpie.dba.dao.FunctionDbField;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

class CustomizedRowMapper implements RowMapper<CustomizedModel> {

    private DbField[] dbFields;

    CustomizedRowMapper(DbField... dbFields)
    {
        this.dbFields = dbFields;
    }

    @Override
    public CustomizedModel mapRow(ResultSet rs, int rowNum)
    {
        CustomizedModel model = new CustomizedModel();

        for (DbField field : dbFields)
        {
            String columnLabel = field instanceof FunctionDbField ? ((FunctionDbField) field).getColumnAlias() : field.getColumnLabel();
            Object value = null;
            try
            {
                value = rs.getObject(columnLabel);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            model.put(columnLabel, value);
        }

        return model;
    }
}
