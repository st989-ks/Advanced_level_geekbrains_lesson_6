package echo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static echo.Server.PORT;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket( "localhost", PORT );
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
                        System.out.println( "Server: " + str );
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
                        System.out.println( "Client: " + str );
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
