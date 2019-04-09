package com.logpie.dba.support;

import java.util.Collection;

public class Assert {

    public static void isNull(Object object, String message)
    {
        if (object != null)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message)
    {
        if (object == null)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, String message)
    {
        if (text == null || text.length() == 0)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array, String message)
    {
        if (array == null || array.length == 0)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message)
    {
        if (collection == null || collection.size() == 0)
        {
            throw new IllegalArgumentException(message);
        }
    }

}
