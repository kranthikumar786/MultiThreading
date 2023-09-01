package Multithreading;

public class PrintNumber1 implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello" + Thread.currentThread().getName());
    }
}
