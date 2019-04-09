package com.logpie.dba.annotation;

import java.lang.annotation.*;

/**
 * specify the mapped database foreign key for a method
 *
 * @author xujiahang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReferencedColumn {

    /**
     * @return referenced table class
     */
    public Class<?> referencedEntityClass();

    /**
     * @return referenced table alias label
     */
    public String referencedEntityAlias() default "";

}
