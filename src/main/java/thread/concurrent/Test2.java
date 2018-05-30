package concurrent;

/**
 * Created by jhui on 2017/12/23.
 */
public class Test2 {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                System.out.println(2);
                System.out.println(3);
            }
        });

        thread.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                System.out.println(2);
                System.out.println(3);
            }
        });

        thread2.start();
    }
}
