package com.minis.test;

import com.minis.ClassPathXmlApplicationContext;

/**
 * @author : hanyang
 * @date : 2023-12-30 12:31
 **/

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aservice = (AService) ctx.getBean("aservice");
        aservice.sayHello();
    }
}
