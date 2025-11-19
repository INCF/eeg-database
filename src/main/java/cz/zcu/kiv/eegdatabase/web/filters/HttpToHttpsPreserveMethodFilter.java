package cz.zcu.kiv.eegdatabase.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpToHttpsPreserveMethodFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getScheme().equals("http")) {
            String method = req.getMethod();
            String url = "https://" + req.getServerName() + req.getRequestURI();

            String qs = req.getQueryString();
            if (qs != null) {
                url += "?" + qs;
            }

            res.setStatus(308); // preserve POST/PUT on redirect
            res.setHeader("Location", url);
            return;
        }

        chain.doFilter(request, response);
    }
}
