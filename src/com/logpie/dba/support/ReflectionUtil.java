package com.logpie.dba.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionUtil {

    private static final String CLASSNAME = ModelUtil.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    private static final String SET = "set";
    private static final String GET = "get";

    /**
     * Build a object by using its default constructor
     *
     * @param clazz
     * @return null if can't build the object.
     */
    public static Object buildInstanceByDefaultConstructor(Class<?> clazz) {

        try {
            final Constructor constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "No default constructor! You need to create a default constructor with no parameters");
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE,"Default constructor can't be accessed. Please set your default constructor as public");
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            LOG.log(Level.SEVERE,"Can't instantiate the class");
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            LOG.log(Level.SEVERE,"InvocationTargetException when calling the constructor");
            ex.printStackTrace();
        }
        return null;
    }

    public static Object runGetter(final Field field, final Object model) {
        Assert.notNull(model, "Model must not be null");

        final String targetMethodName = GET + capitalize(field.getName());

        try {
            final Method getMethod = model.getClass().getMethod(targetMethodName);
            return getMethod.invoke(model);
        } catch (IllegalAccessException e) {
            LOG.log(Level.SEVERE, "Cannot get access to this method, you need to set public to the setters");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Passed illegal argument to this method: " + targetMethodName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            LOG.log(Level.SEVERE, "This method cannot be invoked" + targetMethodName);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            LOG.log(Level.SEVERE, "Cannot find getter method: " + targetMethodName);
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public static void runSetter(final Field field, final Object model, final Class<?> valueClazz, final Object value) {
        Assert.notNull(model, "Model must not be null");

        final String targetMethodName = SET + capitalize(field.getName());

        try {
            final Method setMethod = model.getClass().getMethod(targetMethodName, valueClazz);
            setMethod.invoke(model, value);
            return;
        } catch (IllegalAccessException e) {
            LOG.log(Level.SEVERE, "Cannot get access to this method, you need to set public to the setters");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Passed illegal argument to this method: " + targetMethodName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            LOG.log(Level.SEVERE, "This method cannot be invoked" + targetMethodName);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            LOG.log(Level.SEVERE, "Cannot find setter method: " + targetMethodName);
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

}
