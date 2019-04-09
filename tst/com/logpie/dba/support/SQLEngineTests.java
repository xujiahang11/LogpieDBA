package com.logpie.dba.support;

import com.logpie.dba.core.SQLEngine;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SQLEngine.class)
public class SQLEngineTests {

    @Before
    public void setUp() {
        PowerMockito.spy(SQLEngine.class);
    }

    /*

    @Test
    public void generateWhereClauseTest() {
        SimpleCondition cond = Mockito.mock(SimpleCondition.class);
        Clause.Equal param = Mockito.mock(Clause.Equal.class);

        PowerMockito.when(cond.getClause()).thenReturn(param);

        PowerMockito.when(param.getTableClazz()).thenReturn(new Table(SampleModelWithAGBigIntId.class));
        PowerMockito.when(param.getColumnLabel()).thenReturn("SAMPLE_MODEL_MESSAGE");
        PowerMockito.when(param.getFormatter()).thenReturn(" = 'Hello World'");

        List<String> columns = new LinkedList<>();
        columns.add("SAMPLE_TABLE.SAMPLE_MODEL_MESSAGE");
        columns.add("SAMPLE_TABLE.ERROR_MESSAGE");
        columns.add("ERROR_TABLE.SAMPLE_MODEL_MESSAGE");
        columns.add("SAMPLE_TABLE_WITH_ERROR.SAMPLE_MODEL_MESSAGE");

        String res = SQLEngine.clauseSQL(cond, columns);
        assertEquals("SAMPLE_TABLE.SAMPLE_MODEL_MESSAGE = 'Hello World'", res);
    }

    */
}
