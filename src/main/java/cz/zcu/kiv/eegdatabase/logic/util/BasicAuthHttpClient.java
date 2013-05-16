package cz.zcu.kiv.eegdatabase.logic.util;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.*;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 13.5.13
 * Time: 2:47
 * To change this template use File | Settings | File Templates.
 */
class BasicAuthHttpClient extends DefaultHttpClient {

    private URL url;

    public BasicAuthHttpClient(URL url, String username, String password, ThreadSafeClientConnManager connManager) {
        super(connManager);
        this.url = url;

        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(url.getHost(), AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));
        setCredentialsProvider(credsProvider);
    }

    /*
    @Override
    protected HttpContext createHttpContext() {
        HttpContext context = super.createHttpContext();

        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        authCache.put(targetHost, basicAuth);

        context.setAttribute(ClientContext.AUTH_CACHE, authCache);

        return context;
    }
    */
}