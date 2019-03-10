package com.project.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpConnection {

    void sendGet(String str, String server, String port) throws Exception {

        String url = "http://" + server + ":" + port + str;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println(url);
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        System.out.println();


        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {

            response.append(inputLine);
            String[] mas = inputLine.split(",");
            for (int i = 0; i < mas.length; i++) {
                mas[i] = mas[i].replaceAll("\"", "");
                mas[i] = mas[i].replaceAll("}", "");
                mas[i] = mas[i].replaceAll("\\{", "");
            }
            for (String s : mas)
                System.out.println(s);


        }
        in.close();


    }

}