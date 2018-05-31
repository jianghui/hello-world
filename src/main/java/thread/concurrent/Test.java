package thread.concurrent;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jhui on 2017/7/4.
 */
public class Test {
    ExecutorService exec = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Test test = new Test();
        test.executeRunnable();
        System.out.println("main thread end");
//        test.shutdown();
        System.out.println("exec shutdown");
    }

    public void executeRunnable(){
        for (int i = 9000000; i < 9000100; i++) {
            final int finalI = i;
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("sleep "+ finalI +"s " +Thread.currentThread().getName() + ": execute() runnable");
                }
            });
        }
    }

    public void submitCallable() throws ExecutionException, InterruptedException {
        Future<String> result1 =  exec.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                System.out.println("sleep 3s : submit() Callable");
                return "success";
            }
        });
        Future<String> result2 =  exec.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                System.out.println("sleep 1s : submit() Callable");
                return "success";
            }
        });
        Future<List<String>> result3 =  exec.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                Thread.sleep(2000);
                System.out.println("sleep 2s : submit() Callable");
                List<String> list = Lists.newArrayList();
                list.add("success");
                return list;
            }
        });
    }
    public void submitRunnable(){
        exec.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": submit() runnable");
            }
        });
    }

    public void invokeCallable() throws InterruptedException {
        List<MyCallable> list = new ArrayList<>();
        exec.invokeAll(list);
    }

    public void shutdown() throws InterruptedException {
        if(exec != null && !exec.isShutdown()) {
            exec.shutdown();
            if(!exec.awaitTermination(15L, TimeUnit.SECONDS)) {
                exec.shutdownNow();
            }
        }
    }

    class MyCallable implements Callable<Object>{

        @Override
        public Object call() throws Exception {
            System.out.println(Thread.currentThread().getName() + "：invokeAll() Callable");
            return null;
        }
    }
}
