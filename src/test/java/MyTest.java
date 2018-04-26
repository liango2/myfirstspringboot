import java.io.PrintStream;

/**
 * @author liango
 * @version 1.0
 * @since 2018-01-25 1:13
 */
public class MyTest {
    private String s;

    public static void main(String[] args) {
        MyTest myTest = new MyTest();
        myTest.s = "hi";
        System.out.println("myTest.s = " + myTest.s);
        int i = 3;
        while (i-- > 0) {
            System.out.println("i = " + i);
        }
        say(1, 2);
    }

    public void method() {
        int a = 1;
        int b = 2;
        int c = a+b;
        int d = b+c;
    }

    private int add(int a, int b) {
        return a+b;
    }

    /**
     * @param i
     * @param i1
     */
    private static void say(int i, int i1) {
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i);
        getParam();
        System.out.println(i1);
    }

    public static Object getParam() {
        return 123;
    }
}
