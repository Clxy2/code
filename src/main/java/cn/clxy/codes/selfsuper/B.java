package cn.clxy.codes.selfsuper;

public class B extends A {

	@Override
	public void self1() {
		print("B.self1");
	}

	@Override
	public void super1() {
		print("B.super1");
	}

	@Override
	public void self3() {
		print("B.self3");
		this.self1();
		this.self2();
	}

	@Override
	public void super3() {
		print("B.super3");
		super.super1();
		super.super2();
	}
}
