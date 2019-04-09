package com.logpie.dba.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entity {

    /**
     * @return database table label
     */
    public String table();
}
