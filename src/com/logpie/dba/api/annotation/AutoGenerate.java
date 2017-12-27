package com.logpie.dba.api.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoGenerate {
	/**
	 * declare two auto generate types in the database
	 * 
	 * @author xujiahang
	 *
	 */
	public enum AutoGenerateType {
		NumberAutoIncrement, CurrentTime
	}

	/**
	 * indicates that a key is auto-generated by database
	 * 
	 */
	public AutoGenerateType strategy();
}