package cz.zcu.kiv.eegdatabase.logic.controller.social;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 29.3.11
 * Time: 14:14
 */
public class FBStuffManager implements IFBStuffManager {
    private final String fb_oath_uri =
            "https://graph.facebook.com/ouath/";
    private String secret;
    private String clientId;
    private String redirectURI;

    private String assembleAuthentication(String authCode){
        return fb_oath_uri + "access_token?" +
                "client_id" + clientId +
                "&redirect_uri=" +redirectURI +
                "&client_secret="+secret+
                "&code="+authCode;
    }
    
    
    public String retrieveAccessToken(String code) {
        String accessToken = null;
        if (null != code) {
            String authURL = assembleAuthentication(code);
            URL uri = null;
            try {
                uri = new URL(authURL);
                String result = crawlToUrl(uri);
                String[] values = result.split("&");
                for (String value : values) {
                    String[] valuePair = value.split("=");
                    if (valuePair.length !=2) {
                        throw new RuntimeException("Unexpected Facebook Auth response");
                    } else {
                        if (valuePair[0].equals("access_token")) {
                            accessToken = valuePair[1];
                        }
                    }
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return accessToken;
    }

    private String crawlToUrl(URL uri) {
        ByteArrayOutputStream byteOutputStream = null;
        InputStream inputStream = null;
        String stream = null;
        try {
            inputStream = uri.openStream();
            byteOutputStream = new ByteArrayOutputStream();
            int i;
            while ((i = inputStream.read()) != -1) {
                byteOutputStream.write(i);
            }
            stream = new String(byteOutputStream.toByteArray());
        }   catch (IOException e) {
            throw new RuntimeException(e);
        }   finally {
            if (null != byteOutputStream) {
                try {
                    byteOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stream;
    }

    public String fbLoginAuthenticate() {
        return "redicrect:"+fb_oath_uri+"authorize?"+
                "client_id="+clientId+
                "&display=page&redirect_uri="+redirectURI;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }
}
