package cn.clxy.codes;

public class DispatchTest {
	public static void main(String[] args) {
		Base b = new Sub();// Sub.x = 0, Sub.x = 30
		System.out.println(b.x);// 20
	}
}

class Base {
	int x = 10;

	public Base() {
		this.printMessage();
		x = 20;
	}

	public void printMessage() {
		System.out.println("Base.x = " + x);
	}
}

class Sub extends Base {
	int x = 30;

	public Sub() {
		this.printMessage();
		x = 40;
	}

	public void printMessage() {
		System.out.println("Sub.x = " + x);
	}
}