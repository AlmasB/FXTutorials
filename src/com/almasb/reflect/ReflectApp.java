package com.almasb.reflect;

import java.lang.reflect.Field;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ReflectApp {

    public static void main(String[] args) throws Exception {
        String s = "Hello world";

        Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(s, "Bye world".toCharArray());

        System.out.println("Begin");
        System.out.println(s);
        System.out.println("End");
    }










    /*
    Method method = Class1.class.getDeclaredMethod("someMethod");
        method.setAccessible(true);
        method.invoke(null);
     */
}
