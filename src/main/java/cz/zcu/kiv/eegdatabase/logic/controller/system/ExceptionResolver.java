package cz.zcu.kiv.eegdatabase.logic.controller.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Extends Spring MVC SimpleMappingExceptionResolver to log all received exceptions
 * Created by IntelliJ IDEA.
 * User: jnovotny
 */
public class ExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {
        private Log log = LogFactory.getLog(getClass());


    @Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler,	Exception ex) {

        log.warn("An exception occured", ex);//Log the exception
        ex.printStackTrace();//Show also in the error output

        return super.doResolveException(request,response,handler,ex);
    }
}
