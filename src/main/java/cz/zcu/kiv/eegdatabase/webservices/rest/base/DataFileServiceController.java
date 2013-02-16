package cz.zcu.kiv.eegdatabase.webservices.rest.base;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/base/datafile")
public class DataFileServiceController {

    private final static Log logger = LogFactory.getLog(DataFileServiceController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile f) throws RestServiceException {
        response.addHeader("Location", buildLocation(request, "-12"));
        logger.debug("File upload detected: " + f.getName());
    }

    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{Id}").toString());
        return ut.expand(id).toASCIIString();
    }

}
