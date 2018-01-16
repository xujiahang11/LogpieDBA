package com.logpie.dba.api.annotation;

import java.lang.annotation.*;

/**
 * indicate the field represents primary key in database
 * 
 * @author xujiahang
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ID {

}
