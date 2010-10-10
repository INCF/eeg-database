//$Id: Indexed.java 15636 2008-12-02 14:21:37Z hardy.ferentschik $
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
 * Specifies that an entity is to be indexed by Lucene
 */
public @interface Indexed {
	/**
	 * @return The filename of the index
	 */
	String index() default "";
}
