// $Id: AnalyzerDiscriminator.java 16554 2009-05-13 12:38:19Z hardy.ferentschik $
package org.hibernate.search.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;

import org.hibernate.search.analyzer.Discriminator;

/**
 * Allows to dynamically select a named analyzer through a <code>Discriminator</code> implementation.
 *
 * @author Hardy Ferentschik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface AnalyzerDiscriminator {
	public Class<? extends Discriminator> impl();
}
