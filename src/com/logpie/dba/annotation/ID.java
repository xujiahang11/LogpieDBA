package com.logpie.dba.annotation;

import java.lang.annotation.*;

/**
 * indicate the field represents primary key in database
 *
 * @author xujiahang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ID {

}
