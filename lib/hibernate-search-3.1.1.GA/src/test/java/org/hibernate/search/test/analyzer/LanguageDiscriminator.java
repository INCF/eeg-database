// $Id: LanguageDiscriminator.java 16554 2009-05-13 12:38:19Z hardy.ferentschik $
package org.hibernate.search.test.analyzer;

import org.hibernate.search.analyzer.Discriminator;

/**
 * @author Hardy Ferentschik
 */
public class LanguageDiscriminator implements Discriminator {

	public String getAnanyzerDefinitionName(Object value, Object entity, String field) {
		if ( value == null || !( entity instanceof Article ) ) {
			return null;
		}
		return (String) value;
	}
}
