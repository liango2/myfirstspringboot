import java.util.function.Consumer;

/**
 * @author liango
 * @version 1.0
 * @since 2018-02-03 16:22
 */
public class 对象方法引用 {
    public static void main(String[] args) {
        Consumer<Too> c1 = too -> too.foo();
        Consumer<Too> c2 = too -> new Too2().foo();
        Consumer<Too> c3 = Too::foo;

    }
}

class Too {
    public void foo() {
        System.out.println("invoke");
    }
}

class Too2 {
    public void foo() {
        System.out.println("invoke");
    }
}
