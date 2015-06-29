package com.base.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Route 绑定Controller注解<br>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RouteBind {
	/**对应的路径名 已/开头*/
	String path() default "/";
	/**视图所在目录*/
	String viewPath() default "";
}
