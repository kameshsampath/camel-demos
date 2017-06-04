package org.workspace7.camel.greeter.client;

/**
 * @author kameshs
 */
public class Main {

    private static final int NUMBER_THREADS = 10;

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Threads");

        for (int x = 0; x < NUMBER_THREADS; x++) {
            new Thread(() -> new FeignClient().fire()).run();
        }

        System.out.println(NUMBER_THREADS + " Threads running...");


        while (true) {
            //kill at will
        }
    }
}
