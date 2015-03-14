package cn.clxy.codes.selfsuper;

public class A {

	public void self1() {
		print("A.self1");
	}

	public void self2() {
		print("A.self2");
	}

	public void self3() {
		print("A.self3");
	}

	public void super1() {
		print("A.super1");
	}

	public void super2() {
		print("A.super2");
	}

	public void super3() {
		print("A.super3");
	}

	protected final void print(String method) {
		System.out.println(method);
	}
}
