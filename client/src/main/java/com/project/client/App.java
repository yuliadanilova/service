package com.project.client;


public class App {

    public static void main(String[] args) {

        String DEFAULT_PORT = "8000";
        String DEFAULT_SERVER = "localhost";
        String DEFAULT_VERSION = "1.0.0";
        String server = DEFAULT_SERVER;
        String port = DEFAULT_PORT;
        String version = DEFAULT_VERSION;

        if(args.length==0){
            System.out.println("Programm arguments have no params");
            System.out.println();
            help();
            System.exit(0);
        }

        for(int i=0;i<args.length;i++){
            if(args[i].equals("--port") || args[i].equals("--server") ||
                    args[i].equals("--api_version") || args[i].equals("show") ||
                    args[i].equals("--list") && (args[i+1].isEmpty())){
                System.out.println("Uncorrect programm args");
                help();
                System.exit(0);
            }
        }

        if (!args[0].equals("show") && !args[0].equals("list") && !args[0].equals("help")) {
            System.out.println("Uncorrect input");
            System.exit(0);
        }

        if (args[0].equals("help") && args[1].isEmpty()) {
            System.out.println("Uncorrect input for help");
            System.exit(0);
        }

        if(args[0].equals("help")){
            help();
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--server")) {
                if (i + 1 < args.length) {
                    server = args[i + 1];
                    i++;
                }
            }
            if (args[i].equals("--port")) {
                if (i + 1 < args.length) {
                    port = args[i + 1];
                    i++;
                }
            }

            if (args[i].equals("--api_version")) {
                if (i + 1 < args.length) {
                    version = args[i + 1];
                    i++;
                }
            }
        }


        if (args[0].equals("list")) {
            HttpConnection http2 = new HttpConnection();
            try {
                http2.sendGet("/service/:" + version + "/interfaces", server, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (args[0].equals("show")) {
            HttpConnection http = new HttpConnection();
            try {
                http.sendGet("/service/:" + version + "/interface/:" + args[1], server, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void help(){
        System.out.println("NAME :" + "\n" + "cli_net - client displays network interfaces and info.");
        System.out.println("USAGE :" + "\n" + "  command  [arguments...]");
        System.out.println("VERSION:" + "\n" + "0.0.0");
        System.out.println("COMMAND:" + "\n" + "help, show, list");
        System.out.println("ARGUMENTS ( FOR show COMMAND):" + "\n" + "name " + "--api_version " + "[version] " + "--server " + "[server] " + "--port " + "[port]");
        System.out.println("ARGUMENTS ( FOR list COMMAND):" + "\n" + "--server " + "--api_version " + "[version] " + "[server] " + "--port " + "[port]");
    }

}


