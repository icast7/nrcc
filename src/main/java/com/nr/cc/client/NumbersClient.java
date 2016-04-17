package com.nr.cc.client;

import com.nr.cc.server.NumbersServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class creates an ExecutorService that executes client requests.
 * The clients send a request to the server and print the response value.
 * Created by icastillejos on 4/16/16.
 */
public class NumbersClient {
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {
        NumbersClient numbersClient = new NumbersClient();
        numbersClient.start();
    }

    public void start() {
        //Use max number of threads supported by the server plus 1
        int numberOfThreads = NumbersServer.NUMBER_OF_THREADS + 1;

        System.out.println("#########################################################");
        System.out.println(String.format("# Submitting %d requests to the server on %s:%s #", numberOfThreads, SERVER_HOST, NumbersServer.PORT));
        System.out.println("#########################################################");

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Callable callableHandler = new NumbersClient.NumbersRequestHandler();
            executorService.submit(callableHandler);
        }
        //Shutdown executor
        executorService.shutdown();
    }

    /**
     * This class defines the client requests and prints the server response to the console
     */
    private class NumbersRequestHandler implements Callable<Void> {
        Socket clientSocket = null;

        @Override
        public Void call() {
            try {
                //Connect to the port specified in the Server class
                clientSocket = new Socket(SERVER_HOST, NumbersServer.PORT);

                //Read from the socket and print it
                InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
                System.out.println(reader.read());
            } catch (IOException ex) {
                System.err.println("Unable to write to client on " + clientSocket.getInetAddress());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    //Unable to close connection, intentional noop
                }
            }
            return null;
        }
    }
}
