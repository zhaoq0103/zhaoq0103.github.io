package com.zhaoq.spring3.demo5;

public class Car {
	private String name;
	private Double price;
	
	public Car() {
		super();
	}

	public Car(String name, Double price) {
		super();
		this.name = name;
		this.price = price;
	}

	@Override
	public String toString() {
		return "Car [name=" + name + ", price=" + price + "]";
	}
	
	
}
