// Copyright 2017 logpie.com. All rights reserved.
package com.logpie.dba.annotation;

import java.lang.annotation.*;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * specify the mapped database column for a method
 *
 * @author xujiahang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     *
     * @return database column label
     */
    public String label();

    /**
     *
     * @return database column type
     */
    public DataType type();

    /**
     * declare 6 common types in the database
     *
     * @author xujiahang
     */
    public enum DataType {
        STRING, BOOLEAN, INTEGER, LONG, BIGINT, FLOAT, TIMESTAMP;

        public Class<?> toJavaClass()
        {
            switch (this)
            {
                case STRING:
                    return String.class;
                case BOOLEAN:
                    return Boolean.class;
                case INTEGER:
                    return Integer.class;
                case LONG:
                    return Long.class;
                case BIGINT:
                    return BigInteger.class;
                case FLOAT:
                    return Float.class;
                case TIMESTAMP:
                    return Timestamp.class;

                default:
                    throw new RuntimeException("Incorrect DataType.");
            }
        }
    }
}
