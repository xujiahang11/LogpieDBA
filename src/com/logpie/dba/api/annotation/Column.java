// Copyright 2017 logpie.com. All rights reserved.
package com.logpie.dba.api.annotation;

import java.lang.annotation.*;
import java.sql.Timestamp;

/**
 * specify the mapped database column for a method
 * 
 * @author xujiahang
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

	/**
	 *
	 * @return database key name
	 */
	public String name();

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
		STRING, BOOLEAN, INTEGER, LONG, FLOAT, TIMESTAMP;

		public Class<?> toJavaClass() {
			switch (this) {
				case STRING: return String.class;
				case BOOLEAN: return Boolean.class;
				case INTEGER: return Integer.class;
				case LONG: return Long.class;
				case FLOAT: return Float.class;
				case TIMESTAMP: return Timestamp.class;

				default: throw new RuntimeException("Incorrect DataType.");
			}
		}
	}
}
