import java.io.*;
import Multithreading.*;
public class Main {
public static void main(String[] args){

    PrintNumber1 p1 = new PrintNumber1();
    Thread t = new Thread(p1);
    t.start();
    System.out.println("Hello World");
}
}
