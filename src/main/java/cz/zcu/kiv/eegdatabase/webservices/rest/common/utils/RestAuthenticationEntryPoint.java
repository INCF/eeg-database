package cz.zcu.kiv.eegdatabase.webservices.rest.common.utils;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;


public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    public RestAuthenticationEntryPoint() {
        this.setRealmName("Foo");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("WWW-Authenticate", "FormBased");
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
