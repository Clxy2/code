package cn.clxy.codes.selfsuper;

public class Main {

	public static void main(String[] args) {

		B b = new B();
		C c = new C();

		System.out.println("---- self");
		System.out.println("-- b");
		b.self3();
		System.out.println("-- c");
		c.self3();

		System.out.println("---- super");
		System.out.println("-- b");
		b.super3();
		System.out.println("-- c");
		c.super3();
	}
}
