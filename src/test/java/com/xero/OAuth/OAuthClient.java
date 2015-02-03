package com.xero.OAuth;


import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;


public class OAuthClient {
    
    

    public static void main(String[] args) throws Exception {
        
   
        
        /**
         * The key/secret are created by me for test application, with Application name: TestAppPar
         * 
         */
        OAuthClient client =
                    new OAuthClient("67GXQVPTC78J4LKHJZVAL7SXCMOWTK", "EHA6MOKYFUXKWG1UOOEA2SEAIQ1LRI", 
                            "https://api.xero.com/oauth/RequestToken", 
                            "https://api.xero.com/oauth/AccessToken");
        Map<String,String> requestTokenData = client.getRequestToken();
        
        System.out.println("Request Token Data="+ requestTokenData);
    }

    
    
    /**
     * Get a request token for TestAppPar application API.
     * @return Map of key/value pairs.
     * @throws Exception
     */
    public Map<String,String> getRequestToken() 
        throws Exception
    {
        
        URL url = new URL(requestTokenEndPoint);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(0);
        uc.setReadTimeout(0);
        uc.setDoOutput(true);
        OutputStreamWriter uco = new OutputStreamWriter
            (uc.getOutputStream());

        Request op = new Request("POST");
        op.addParameter("oauth_consumer_key", key);
        String cburl = "xero.com"; // callback URL.
        
        op.addParameter("oauth_callback", cburl);
        

        op.sign(requestTokenEndPoint, secret, null);
        uco.write(op.toString());// flush the normalized string to url connection.
        uco.close();

        //make sure you save these variable for the current user session. It has following things.
        // sf_request_auth_url -> To where you should be redirecting the browser so that user can login to TestAppPar
        // oauth_token -> Request Token [ Important! Needed while making a access-token request, after the login ]
        // oauth_token_secret -> Request Token secret [ Important! Needed while making a access-token request, after the login ]
        return Utils.readParams(uc.getInputStream());
        
    }

    
    
    /**
     * Following method is used to get an access token from Xero server!!
     * Note: I have passed the requestToken, requestTokenSecret, and requestTokenVerifier that xero API gives.
     *       Complete the following steps before calling this method.
     *          - Request a request token 
     *          - Get the 'sf_request_auth_url' from response of request token
     *          - Redirect the browser to 'sf_request_auth_url' from Publisher
     *          - After the customer logs in successfully, Publisher redirects back to the app at the 'oauth_callback' URL you provided while making the request token call.
     *          - When the redirect happens, the callback URL will contain the following parameters
     *              - oauth_token
     *              - oauth_token_verifier
     *          - The callback is the authorization hand-off from Publisher to your app.     
     *          
     * @param requestToken
     * @param requestTokenSecret
     * @param requestTokenVerifier
     * @return Map<String,String> params from TestAppPar containing access-token and access-token-secret.
     * @throws Exception 
     */
    public Map<String,String> getAccessToken(
            String requestToken, 
            String requestTokenSecret,
            String requestTokenVerifier) 
            throws Exception
    {
        URL url = new URL(accessTokenEndPoint);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(0);
        uc.setReadTimeout(0);
        uc.setDoOutput(true);
        OutputStreamWriter uco = new OutputStreamWriter
            (uc.getOutputStream());

        Request op = new Request("POST");
        op.addParameter("oauth_consumer_key", key);
        op.addParameter("oauth_token", requestToken);
        op.addParameter("oauth_verifier", requestTokenVerifier);
        op.sign(key, secret, requestTokenSecret);
        uco.write(op.toString());
        uco.close();

        Map<String,String> params = Utils.readParams(uc.getInputStream());
        //Save the following parameters to the current browsing user session! Your app will need them for making API calls.

        String accessToken = Utils.verifyGet("oauth_token", params);
        String accessTokenSecret = Utils.verifyGet("oauth_token_secret", params);
        String restEndPoint = Utils.verifyGet("sf_opensocial_rest_url", params);
        String uploadEndPoint = Utils.verifyGet("sf_upload_url", params);
        return params;
        
    }
    

    /**
     * Return the consumer key
     * @return String
     */
    public String getConsumerKey() {
        return key ;
    }
    /**
     * Return the consumer secret
     * @return String
     */
    public String getConsumerSecret() {
        return secret;
    }
    /**
     * Return the request token end point
     * @return String
     */
    public String getRequestTokenEndPoint() {
        return requestTokenEndPoint ;
    }
    /**
     * Return access token end point
     * @return String
     */
    public String getAccessTokenEndPoint() {
        return accessTokenEndPoint;
    }
    /**
     * The Consumer key
     */
    private final String key ;
    /**
     * The Consumer secret
     */
    private final String secret ;
    /**
     * OAuth1.0a request token end point, provided by Snapfish
     */
    private final String requestTokenEndPoint ;
    /**
     * OAuth1.0a access token end point, provided by Snapfish
     */
    private final String accessTokenEndPoint;
    
    
    /**
     * The default constructor.
     * @param k : Consumer key
     * @param s : Consumer secret
     * @param reqTokenEndPt : RequestToken end point
     * @param accTokenEndPt : AccessToken end point
     */
    public OAuthClient(String k, String s, String reqTokenEndPt,String accTokenEndPt) {
        key = k ;
        secret = s;
        requestTokenEndPoint = reqTokenEndPt ;
        accessTokenEndPoint = accTokenEndPt ;
    }
    
    
   
}// end of the class
