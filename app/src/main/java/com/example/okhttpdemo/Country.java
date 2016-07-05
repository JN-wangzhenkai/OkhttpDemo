package com.example.okhttpdemo;

/**
 * Created by wangzhenkai on 2016/7/5.
 */
public class Country {
    public  String login;
    public String contributions;

   // 因为在java中 Object类是基类，所以每个类都会有toString方法。
    //String类重写了Object的toString方法，用于返回String的字符串值。
    //因为它是object里面已经有了的方法，而所有类都是继承object，所以“所有对象都有这个方法”
    @Override
    public String toString() {
        return login+","+contributions;
    }

}
