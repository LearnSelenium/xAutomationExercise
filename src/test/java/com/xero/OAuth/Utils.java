package com.xero.OAuth;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class Utils {

    public final static String decode(String s) {
        if (s == null) {
            return "";
        }

        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow);
        }
    }

    public final static String encode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    // OAuth encodes some characters differently:
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow);
        }
    }

    public static Map<String, String> readParams(InputStream inp)
            throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inp));

        String line = in.readLine();
        in.close();

        System.out.println("Params `" + line + "'");

        String kv[] = line.split("&");

        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < kv.length; i++) {
            String v[] = kv[i].split("=");
            if (v.length != 2) {
                throw new RuntimeException("Unexpected response `" + line + "'");
            }
            

            params.put(decode(v[0]), decode(v[1]));
        }

        return params;
    }

    public final static String verifyGet(String k, Map<String,String> p)
    {
        String ret = p.get(k);
        if (ret == null) {
            throw new RuntimeException("Missing parameter `"+k+"'");
        }
        return ret;
    }
}
