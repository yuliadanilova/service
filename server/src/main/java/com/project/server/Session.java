package com.project.server;


import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;

import static java.net.NetworkInterface.getByName;
import static java.net.NetworkInterface.getNetworkInterfaces;


public class Session {

    public Session(InputStream in,OutputStream out)  {
        this.in=in;
        this.out=out;
    }


    public void start(String version) {

        try {
            String header = readHeader();
            String url = getURIFromHeader(header);
            StringTokenizer st = new StringTokenizer(url, "/:");
            String ver = "";
            while (st.hasMoreTokens()) {
                String key = st.nextToken();
                if (key.equals("service")) {
                    ver = st.nextToken();
                    break;
                }
            }
            if (ver.equals(version)) {
                int code = send(url);
            } else
                System.out.println(getAnswer(404));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readHeader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String ln = null;
        while (true) {
            ln = reader.readLine();
            if (ln == null || ln.isEmpty()) {
                break;
            }
            builder.append(ln + System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    public String getURIFromHeader(String header) {

        int from = header.indexOf(" ") + 1;
        int to = header.indexOf(" ", from);
        String uri = header.substring(from, to);
        int paramIndex = uri.indexOf("?");
        if (paramIndex != -1) {
            uri = uri.substring(0, paramIndex);
        }

        return uri;
    }

    public JSONObject getStream(String url) throws SocketException {

        JSONObject json = new JSONObject();
        ArrayList listIpv4 = new ArrayList();
        ArrayList listIpv6 = new ArrayList();

        if (url.contains("/interfaces")) {
            try {
                ArrayList list = new ArrayList();
                Enumeration<NetworkInterface> net = getNetworkInterfaces();
                while (net.hasMoreElements()) {
                    NetworkInterface networkInterface = net.nextElement();
                    list.add(networkInterface);
                }
                json.put("interfaces", list);


            } catch (SocketException e) {
                e.printStackTrace();
            }

        } else {
            if (url.contains("/interface/:")) {
                String s = url.substring(url.indexOf("interface/:"));

                int from = s.indexOf(":") + 1;
                int to = s.length();
                String name = s.substring(from, to);

                NetworkInterface network = getByName(name);
                json.put("name", name);

                byte[] mac = network.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                }
                json.put("hw_addr", sb.toString());


                Enumeration<InetAddress> addres = network.getInetAddresses();
                InetAddress currentAddres;
                while (addres.hasMoreElements()) {
                    currentAddres = addres.nextElement();
                    if (currentAddres instanceof Inet6Address)

                        listIpv6.add(currentAddres.toString());
                    else

                        listIpv4.add(currentAddres.toString());
                }
                json.put("ipv4", listIpv4);
                json.put("ipv6", listIpv6);

                String mtu = String.valueOf(network.getMTU());
                json.put("MTU", mtu);


            }

        }

        return json;


    }

    public int send(String url) throws IOException {

        JSONObject object;
        object = getStream(url);
        StringEntity se = new StringEntity(object.toString());
        InputStream strm = se.getContent();
        int code = (strm != null) ? 200 : 404;
        String header = getHeader(code);
        PrintStream answer = new PrintStream(out, true, "UTF-8");
        answer.print(header);
        if (code == 200) {
            int count = 0;
            byte[] buffer = new byte[1024];
            while ((count = strm.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            strm.close();
        }
        return code;
    }

    public String getHeader(int code) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("HTTP/1.1 " + code + " " + getAnswer(code) + "\n");
        buffer.append("Date: " + new Date().toGMTString() + "\n");
        buffer.append("Accept-Ranges: none\n");
        buffer.append("\n");

        return buffer.toString();
    }

    public String getAnswer(int code) {
        switch (code) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            default:
                return "Internal Server Error";
        }
    }


    private InputStream in = null;
    private OutputStream out = null;

}
