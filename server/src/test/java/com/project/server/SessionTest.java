package com.project.server;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;


public class SessionTest {



    @Test
    public void getURIFromHeader() {
       try {
           String hed="GET /service/:1.0.0/interface/:lo0 HTTP/1.1\n" +
                   "User-Agent: Java/11.0.2\n" +
                   "Host: localhost:8060\n" +
                   "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\n" +
                   "Connection: keep-alive";
           byte[] inBuf = new byte[4096];
           InputStream in = new ByteArrayInputStream(inBuf);
           OutputStream out = new ByteArrayOutputStream(4096);
           Session session = new Session(in, out);
           String actual = session.getURIFromHeader(hed);
           String exp="/service/:1.0.0/interface/:lo0";
           assertEquals(actual,exp);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Test
    public void getStream() {
        try {
            String url = "/service/:1.0.0/interface/:lo0";
            byte[] inBuf = new byte[4096];
            InputStream in = new ByteArrayInputStream(inBuf);
            OutputStream out = new ByteArrayOutputStream(4096);
            Session session = new Session(in, out);
            JSONObject object = session.getStream(url);
            String exp = "{\"hw_addr\":\"\",\"ipv4\":[\"\\/127.0.0.1\"]," +
                    "\"ipv6\":[\"\\/fe80:0:0:0:0:0:0:1%lo0\"," +
                    "\"\\/0:0:0:0:0:0:0:1%lo0\"],\"name\":\"lo0\",\"MTU\":\"16384\"}";
            assertEquals(object.toString(), exp);

        } catch (Exception t) {
            t.printStackTrace();
        }
    }

    @Test
    public void send() {
        try {
            byte[] inBuf = new byte[4096];
            InputStream in = new ByteArrayInputStream(inBuf);
            OutputStream out = new ByteArrayOutputStream(4096);
            Session session = new Session(in, out);
            int actual=session.send("/service/:1.0.0/interface/:lo0");
            int expected=200;

            assertEquals(expected,actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHeader() {
        try {
            byte[] inBuf = new byte[4096];
            InputStream in = new ByteArrayInputStream(inBuf);
            OutputStream out = new ByteArrayOutputStream(4096);
            Session session = new Session(in, out);
            String actual = session.getHeader(200);
            boolean firstpart = actual.startsWith("HTTP/1.1 200 OK\n", 0);
            boolean secondpart = actual.endsWith("Accept-Ranges: none\n\n");

            assertEquals(firstpart,true);
            assertEquals(secondpart,true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getAnswer() {
        try {

            byte[] inBuf = new byte[4096];
            InputStream in = new ByteArrayInputStream(inBuf);
            OutputStream out = new ByteArrayOutputStream(4096);
            Session session = new Session(in, out);
            String actual = session.getAnswer(404);
            String expected = "Not Found";
            assertEquals(actual, expected);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}