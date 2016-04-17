package com.nr.cc.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This server class accepts connection on PORT and responds with an integer value of the Second in the current Date
 * Created by icastillejos on 4/16/16.
 */
public class NumbersServer {
    //Set the server port
    public static final int PORT = 8087;

    //Set the number of threads (connections)
    public static final int NUMBER_OF_THREADS = 3;

    private static final Logger logger = Logger.getLogger(NumbersServer.class.getCanonicalName());

    public static void main(String args[]) {
        //Start server
        NumbersServer numbersServer = new NumbersServer();
        numbersServer.start();
    }

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(NumbersServer.NUMBER_OF_THREADS);
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            logger.info(String.format("Accepting connections on port %d", serverSocket.getLocalPort()));
            while (true) {
                try {
                    //Accept the connection and submit it to the ExecutorService
                    Socket request = serverSocket.accept();
                    Callable callableHandler = new NumbersResponseHandler(request);
                    executorService.submit(callableHandler);
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to accept connection.", ex);
                } catch (RuntimeException rtex) {
                    logger.log(Level.SEVERE, "Unexpected error condition.", rtex);

                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to start NumbersServer.", ex);
        }
    }

    /**
     * This class defines the server response
     */
    private class NumbersResponseHandler implements Callable<Void> {
        private final Socket request;

        public NumbersResponseHandler(Socket request) {
            this.request = request;
        }

        @Override
        public Void call(){
            try {
                logger.log(Level.INFO, "Connected" + request.getInetAddress());
                OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
                //Using the deprecated method just for the sake of 'randomizing' the int response
                writer.write(new Date().getSeconds());
                writer.flush();
                //Sleep for a number seconds to show the server won't handle more than NUMBER_OF_THREADS connections
                TimeUnit.SECONDS.sleep(5);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to write to client on " + request.getInetAddress());
            } catch (InterruptedException ex) {
                logger.log(Level.WARNING, "Thread sleep interrupted");
            } finally {
                try {
                    request.close();
                } catch (IOException ex) {
                    //Unable to close connection, intentional noop
                }
            }
            return null;
        }
    }
}
