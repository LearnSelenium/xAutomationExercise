package com.xero.OAuth;


import java.net.URL;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Encapsulate the request params and help to provide signature methods!
 */
public class Request {
    public Request(String m)
    { 
        httpMethod = m;
        m_params = new TreeSet<String>(); }

    public void addParameter(String k, String v)
    { m_params.add(Utils.encode(k)+"="+Utils.encode(v)); }

    
    /**
     * @param url
     * @param consumer_secret
     * @param token_secret
     */
    public void sign(String url, String consumer_secret, String token_secret)
    {
        StringBuffer param = new StringBuffer();

        // set/add standard parameters.
        addParameter("oauth_timestamp", ""+(System.currentTimeMillis()/1000));
        addParameter("oauth_signature_method", "HMAC-SHA1");
        addParameter("oauth_version", "1.0");
        addParameter("oauth_nonce", "x"+System.currentTimeMillis());// make sure you generate a new nonce everytime!

        boolean first = true;

        StringBuffer base = new StringBuffer();

        base.append(Utils.encode(httpMethod));
        base.append("&");

        base.append(Utils.encode(normalize(url)));
        base.append("&");
        
        for (String p: m_params) {
            if (p.equals("oauth_signature")) { continue; }
            if (first) { first = false; }
            else { param.append("&"); }
            param.append(p);
        }
        
        base.append(Utils.encode(param.toString()));

        System.out.println("Signing `"+base+"'");

        String key = Utils.encode(consumer_secret)+"&"+
                Utils.encode(token_secret);
        System.out.println("with `"+key+"'");
        
        try {
            Mac mac = Mac.getInstance(MAC_NAME);
            SecretKey skey = new SecretKeySpec(key.getBytes("UTF-8"), MAC_NAME);
            mac.init(skey);
            String signature =
                new String
                (BASE64.encode
                 (mac.doFinal
                  (base.toString().getBytes("UTF-8"))));
            addParameter("oauth_signature", signature);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }    
    
    /**
     * @param String
     * @return String : Normalized string
     */
    private final String normalize(String u)
    {
        try {
            URL url = new URL(u);
            StringBuffer sb = new StringBuffer();
            sb.append(url.getProtocol().toLowerCase());
            sb.append("://");
            sb.append(url.getHost().toLowerCase());
            if ((url.getPort() == -1) ||
                ((url.getPort() == 80) &&
                 (url.getProtocol().equalsIgnoreCase("http"))) ||
                ((url.getPort() == 443) &&
                 (url.getProtocol().equalsIgnoreCase("https")))) {
            }
            else {
                sb.append(":"+url.getPort());
            }
            if (url.getPath() != null) {
                sb.append(url.getPath());
                
                if (url.getQuery() != null) {
                    String[] indivdualQuery = url.getQuery().split("&");
                    
                    for (String iq: indivdualQuery) {
                        if(iq != null)
                        {
                            String[] paramSplit = iq.split("=");
                            if(paramSplit != null)
                            {
                                this.addParameter(paramSplit[0], paramSplit[1]);
                            }
                        }
                    }
                }
            }
            System.out.println(u +" -> "+sb.toString());
            return sb.toString();
        }
        catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (String p: m_params) {
            if (first) { first = false; }
            else { sb.append("&"); }
            sb.append(p);
        }
        return sb.toString();
    }


    private final SortedSet<String> m_params;
    //private final boolean m_isget;
    private final String httpMethod;

    private static final String MAC_NAME = "HmacSHA1";
    private static final Base64 BASE64 = new Base64();

}
