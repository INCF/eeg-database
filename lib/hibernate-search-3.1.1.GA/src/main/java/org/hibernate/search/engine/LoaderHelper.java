// $Id: LoaderHelper.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.common.util.ReflectHelper;

/**
 * @author Emmanuel Bernard
 */
public abstract class LoaderHelper {
	private static final List<Class> objectNotFoundExceptions;

	static {
		objectNotFoundExceptions = new ArrayList<Class>(2);
		try {
			objectNotFoundExceptions.add(
					ReflectHelper.classForName( "org.hibernate.ObjectNotFoundException" )
			);
		}
		catch (ClassNotFoundException e) {
			//leave it alone
		}
		try {
			objectNotFoundExceptions.add(
					ReflectHelper.classForName( "javax.persistence.EntityNotFoundException" )
			);
		}
		catch (ClassNotFoundException e) {
			//leave it alone
		}
	}

	public static boolean isObjectNotFoundException(RuntimeException e) {
		boolean objectNotFound = false;
		Class exceptionClass = e.getClass();
		for ( Class clazz : objectNotFoundExceptions) {
			if ( clazz.isAssignableFrom( exceptionClass ) ) {
				objectNotFound = true;
				break;
			}
		}
		return objectNotFound;
	}
}
