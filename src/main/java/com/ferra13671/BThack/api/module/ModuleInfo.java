package com.ferra13671.BThack.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();
    String description() default "";
    int key() default 0;
    String category();
    boolean autoEnabled() default false;
    boolean visible() default true;
    boolean allowRemapVisible() default true;
    boolean allowRemapKeyCode() default true;
}
