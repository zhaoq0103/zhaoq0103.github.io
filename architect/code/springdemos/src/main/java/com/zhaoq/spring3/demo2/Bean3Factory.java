package com.zhaoq.spring3.demo2;
/**
 * 使用实例工厂
 *
 */
public class Bean3Factory {
	public Bean3 getBean3(){
		System.out.println("Bean3实例工厂的getBean3方法...");
		return new Bean3();
	}
}
