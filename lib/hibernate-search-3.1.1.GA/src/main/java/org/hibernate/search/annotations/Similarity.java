// $Id: Similarity.java 15624 2008-11-27 13:12:23Z hardy.ferentschik $
package org.hibernate.search.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@Documented
/**
 * Specifies a similarity implementation to use.
 *
 * @author Nick Vincent
 */
public @interface Similarity {
	public Class impl();
}
