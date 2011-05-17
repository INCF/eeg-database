//$Id: NumberBridge.java 14707 2008-05-29 11:50:15Z hardy.ferentschik $
package org.hibernate.search.bridge.builtin;

import org.hibernate.search.bridge.TwoWayStringBridge;

/**
 * Base class for numbers - integer, double, etc.
 * 
 * @author Emmanuel Bernard
 */
public abstract class NumberBridge implements TwoWayStringBridge {
	public String objectToString(Object object) {
		return object != null ?
				object.toString() :
				null;
	}
}
