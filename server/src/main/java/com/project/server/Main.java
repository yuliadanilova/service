package com.project.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    public static void main(String[] args) {
        int DEFAULT_PORT = 8000;
        String DEF_VERSION = "1.0.0";
        int port = DEFAULT_PORT;
        String version = DEF_VERSION;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--port")) {
                port = Integer.parseInt(args[i + 1]);
                if (i + 1 < args.length)
                    i++;
            }

            if (args[i].equals("api_version")) {
                version = args[i + 1];
                if (i + 1 < args.length)
                    i++;
            }
        }
        System.out.println(port);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + serverSocket.getLocalPort() + "\n");
        } catch (IOException e) {
            System.out.println("Port " + port + " is blocked.");
            System.exit(-1);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                InputStream in=clientSocket.getInputStream();
                OutputStream out=clientSocket.getOutputStream();
                Session session = new Session(in,out);
                session.start(version);
            } catch (IOException e) {
                System.out.println("Failed to establish connection.");
                System.out.println(e.getMessage());
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
