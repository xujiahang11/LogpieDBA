package com.logpie.dba.support;

import com.logpie.dba.sample.SampleModelWithAGBigIntId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReflectionUtil.class)
public class ReflectionUtilTests {

    private SampleModelWithAGBigIntId model;

    @Before
    public void setUp() {
        model = new SampleModelWithAGBigIntId();
        PowerMockito.spy(ReflectionUtil.class);
    }

    @Test
    public void testRunGetterOnBigIntegerField() throws Exception {
        Field idField = model.getClass().getDeclaredField("id");
        BigInteger bigint = new BigInteger(20, new Random());
        model.setId(bigint);

        Object id = ReflectionUtil.runGetter(idField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(bigint, (BigInteger) id);
    }

    @Test
    public void testRunGetterOnStringField() throws Exception {
        Field msgField = model.getClass().getDeclaredField("message");
        String msg = "Hello World.";
        model.setMessage(msg);

        Object message = ReflectionUtil.runGetter(msgField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(msg, (String) message);
    }

    @Test
    public void testRunGetterOnBooleanField() throws Exception {
        Field boolField = model.getClass().getDeclaredField("isTrue");
        model.setIsTrue(new Boolean(true));

        Object isTrue = ReflectionUtil.runGetter(boolField, model);
        verifyCapitalizeMethodCalled();
        assertTrue(((Boolean)isTrue).booleanValue());
    }

    @Test
    public void testRunGetterOnIntegerField() throws Exception {
        Field intField = model.getClass().getDeclaredField("intNumber");
        Integer i = new Integer(2018);
        model.setIntNumber(i);

        Object number = ReflectionUtil.runGetter(intField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(i, (Integer) number);
    }

    @Test
    public void testRunGetterOnLongField() throws Exception {
        Field longField = model.getClass().getDeclaredField("longNumber");
        Long l = new Long(2018);
        model.setLongNumber(l);

        Object number = ReflectionUtil.runGetter(longField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(l, (Long) number);
    }

    @Test
    public void testRunGetterOnFloatField() throws Exception {
        Field floatField = model.getClass().getDeclaredField("floatNumber");
        Float f = new Float(2018.0);
        model.setFloatNumber(f);

        Object number = ReflectionUtil.runGetter(floatField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(f, (Float) number);
    }

    @Test
    public void testRunGetterOnTimestampField() throws Exception {
        Field timeField = model.getClass().getDeclaredField("timestamp");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        model.setTimestamp(time);

        Object timestamp = ReflectionUtil.runGetter(timeField, model);
        verifyCapitalizeMethodCalled();
        assertEquals(time, (Timestamp) timestamp);
    }

    @Test
    public void testRunGetterOnFieldWithPrivateGetter() throws Exception {
        Field privateGetterField = model.getClass().getDeclaredField("messageWithPrivateGetterAndSetter");
        boolean illegalAccessExceptionHappened = isRunGetterExceptionsHappened(privateGetterField);
        assertTrue(illegalAccessExceptionHappened);
    }

    @Test
    public void testRunGetterOnFieldWithoutGetter() throws Exception {
        Field nonExistingGetterField = model.getClass().getDeclaredField("messageWithNoGetterAndSetter");
        boolean noSuchMethodExceptionHappened = isRunGetterExceptionsHappened(nonExistingGetterField);
        assertTrue(noSuchMethodExceptionHappened);
    }

    @Test
    public void testRunGetterOnFieldWithIllegalArgumentExceptionGetter() throws Exception{
        Field illegalArgumentExceptionGetterField = model.getClass().getDeclaredField("illegalArgumentException");
        boolean illegalArgumentExceptionHappened = isRunGetterExceptionsHappened(illegalArgumentExceptionGetterField);
        assertTrue(illegalArgumentExceptionHappened);
    }

    private boolean isRunGetterExceptionsHappened(Field field) throws Exception{
        boolean exceptionHappened = false;
        try {
            ReflectionUtil.runGetter(field, model);
            verifyCapitalizeMethodCalled();
        } catch(RuntimeException e) {
            exceptionHappened = true;
        }
        return exceptionHappened;
    }

    @Test
    public void testRunSetterOnBigIntegerField() throws Exception {
        Field idField = model.getClass().getDeclaredField("id");
        BigInteger bigint = new BigInteger(20, new Random());

        ReflectionUtil.runSetter(idField, model, BigInteger.class, bigint);
        verifyCapitalizeMethodCalled();
        assertEquals(bigint, model.getId());
    }

    @Test
    public void testRunSetterOnStringField() throws Exception {
        Field msgField = model.getClass().getDeclaredField("message");
        String msg = "Hello World, again.";

        ReflectionUtil.runSetter(msgField, model, String.class, msg);
        verifyCapitalizeMethodCalled();
        assertEquals(msg, model.getMessage());
    }

    @Test
    public void testRunSetterOnBooleanField() throws Exception {
        Field boolField = model.getClass().getDeclaredField("isTrue");

        ReflectionUtil.runSetter(boolField, model, Boolean.class, true);
        verifyCapitalizeMethodCalled();
        assertTrue(model.getIsTrue().booleanValue());
    }

    @Test
    public void testRunSetterOnIntegerField() throws Exception {
        Field intField = model.getClass().getDeclaredField("intNumber");
        Integer i = new Integer(2017);

        ReflectionUtil.runSetter(intField, model, Integer.class, i);
        verifyCapitalizeMethodCalled();
        assertEquals(i, model.getIntNumber());
    }

    @Test
    public void testRunSetterOnLongField() throws Exception {
        Field longField = model.getClass().getDeclaredField("longNumber");
        Long l = new Long(2017);

        ReflectionUtil.runSetter(longField, model, Long.class, l);
        verifyCapitalizeMethodCalled();
        assertEquals(l, model.getLongNumber());
    }

    @Test
    public void testRunSetterOnFloatField() throws Exception {
        Field floatField = model.getClass().getDeclaredField("floatNumber");
        Float f = new Float(2017.0);

        ReflectionUtil.runSetter(floatField, model, Float.class, f);
        verifyCapitalizeMethodCalled();
        assertEquals(f, model.getFloatNumber());
    }

    @Test
    public void testRunSetterOnTimestampField() throws Exception {
        Field timeField = model.getClass().getDeclaredField("timestamp");
        Timestamp time = new Timestamp(System.currentTimeMillis());

        ReflectionUtil.runSetter(timeField, model, Timestamp.class, time);
        verifyCapitalizeMethodCalled();
        assertEquals(time, model.getTimestamp());
    }

    @Test
    public void testRunSetterOnFieldWithIllegalArgument() throws Exception {
        Field idField = model.getClass().getDeclaredField("id");
        boolean illegalArgumentExceptionHappened = isRunSetterExceptionsHappened(idField, String.class, "unmatched argument");
        assertTrue(illegalArgumentExceptionHappened);
    }

    @Test
    public void testRunSetterOnFieldWithPrivateSetter() throws Exception {
        Field privateSetterField = model.getClass().getDeclaredField("messageWithPrivateGetterAndSetter");
        boolean illegalAccessExceptionHappened = isRunSetterExceptionsHappened(privateSetterField, String.class, "override message");
        assertTrue(illegalAccessExceptionHappened);
        assertEquals(model.checkPrivateMessageForTest(), "private message");
    }

    @Test
    public void testRunSetterOnFieldWithoutSetter() throws Exception {
        Field nonExistingSetterField = model.getClass().getDeclaredField("messageWithNoGetterAndSetter");
        boolean noSuchMethodExceptionHappened = isRunSetterExceptionsHappened(nonExistingSetterField, String.class, "override message");
        assertTrue(noSuchMethodExceptionHappened);
        assertEquals(model.checkNoSetterMessageForTest(), "message with no getter and setter");
    }

    private boolean isRunSetterExceptionsHappened(Field field, Class<?> clazz, Object arg) throws Exception{
        boolean exceptionHappened = false;
        try {
            ReflectionUtil.runSetter(field, model, clazz, arg);
            verifyCapitalizeMethodCalled();
        } catch(RuntimeException e) {
            exceptionHappened = true;
        }
        return exceptionHappened;
    }

    private void verifyCapitalizeMethodCalled() throws Exception{
        PowerMockito.verifyPrivate(ReflectionUtil.class, Mockito.times(1)).invoke("capitalize", anyString());
    }
}
