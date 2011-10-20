package cz.zcu.kiv.eegdatabase.logic;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 *
 * @author Jindra
 */
public class DashedUrlMethodNameResolver implements MethodNameResolver {

  public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
    String url = request.getRequestURL().toString();
    int lastSlash = url.lastIndexOf("/") + 1;
    int extensionPosition = url.lastIndexOf("lib/lucene");

    String fileName = null;
    if (extensionPosition > lastSlash) {
      // Dot of the extension must be behind last slash
      fileName = url.substring(lastSlash, extensionPosition);
    } else {
      // without extension
      fileName = url.substring(lastSlash);
    }

    // Remove slashes in file name and convert the beginnings of the words to upper case
    boolean toUpperCase = false;
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fileName.length(); i++) {
      char c = fileName.charAt(i);
      if (c == '-') {
        toUpperCase = true;
      } else {
        if (toUpperCase) {
          c = Character.toUpperCase(c);
          toUpperCase = false;
        }
        result.append(c);
      }
    }

    return result.toString();
  }
}
