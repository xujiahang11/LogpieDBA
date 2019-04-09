package com.logpie.dba.domain;

import com.logpie.dba.dao.Clause;
import com.logpie.dba.core.Table;
import com.logpie.dba.sample.SampleModelWithAGBigIntId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class SimpleClauseTests {

    private SampleModelWithAGBigIntId model;
    private Table expectedTable;

    @Before
    public void setUp() {
        model = Mockito.mock(SampleModelWithAGBigIntId.class);
        expectedTable = new Table(model.getClass());
    }

    @Test
    public void testEqualParamWithIntValue() {
        String column = "Test_Column";
        int value = 100;
        Clause Clause = new Clause.Equal(model.getClass(), column, value);

        Table expectedTable = new Table(model.getClass());
        assertEquals(expectedTable, Clause.getTable());

        assertEquals(column, Clause.getColumnLabel());

        String expectedClause = column + " = 100";
        String[] arg = { column };
        assertEquals(expectedClause, Clause.formatter().format(arg));
    }

    /*

    @Test
    public void testEqualParamWithStringValue() {
        String column = "Test_Column";
        String value = "Test_Value";
        SqlClause sqlClause = new Equal(model.getClass(), column, value);

        String expectedClause = " = 'Test_Value'";
        assertEquals(expectedClause, sqlClause.getFormatter());
    }

    @Test
    public void testEqualParamWithTimestampValue() {
        String column = "Test_Column";
        Timestamp value = new Timestamp(System.currentTimeMillis());
        SqlClause sqlClause = new Equal(model.getClass(), column, value);

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(value);
        String expectedClause = " = " + time;
        assertEquals(expectedClause, sqlClause.getFormatter());
    }

    @Test
    public void testInParamWithIntValue() {
        String column = "Test_Column";
        List<Integer> values = new LinkedList<>();
        for(int i = 1; i < 5; i++)
            values.add(i);
        SqlClause sqlClause = new In(model.getClass(), column, values);

        String expectedClause = " IN (1, 2, 3, 4)";
        assertEquals(expectedClause, sqlClause.getFormatter());
    }

    @Test
    public void testInParamWithStringValue() {
        String column = "Test_Column";
        List<String> values = new LinkedList<>();
        for(int i = 1; i < 5; i++)
            values.add(String.valueOf(i));
        SqlClause sqlClause = new In(model.getClass(), column, values);

        String expectedClause = " IN ('1', '2', '3', '4')";
        assertEquals(expectedClause, sqlClause.getFormatter());
    }


    */
}
