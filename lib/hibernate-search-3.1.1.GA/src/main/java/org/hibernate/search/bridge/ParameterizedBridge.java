//$Id: ParameterizedBridge.java 15528 2008-11-06 23:11:06Z sannegrinovero $
package org.hibernate.search.bridge;

import java.util.Map;

/**
 * Allow parameter injection to a given bridge.
 * 
 * Implementors need to be threadsafe, but the
 * setParameterValues method doesn't need any
 * guard as initialization is always safe.
 *
 * @author Emmanuel Bernard
 */
public interface ParameterizedBridge {
	//TODO inject Properties? since the annotations cannot support Object attribute?
	void setParameterValues(Map parameters);
}
