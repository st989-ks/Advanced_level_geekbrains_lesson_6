package echo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    final static int PORT = 3000;

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket( PORT );
            System.out.println( "Server started" );
            socket = server.accept();
            System.out.println( "Client connected" );

            Scanner in = new Scanner( socket.getInputStream() );
            PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
            Scanner console = new Scanner( System.in );

            Thread t1 = new Thread( new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine();

                        if (str.equals( "/end" )) {
                            break;
                        }

                        System.out.println( "Client: " + str );
                    }
                }
            } );
            t1.start();

            Thread t2 = new Thread( new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = console.nextLine();
                        out.println( str );
                        System.out.println( "Server: " + str );
                    }
                }
            } );
            t2.setDaemon( true );
            t2.start();

            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

