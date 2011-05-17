// $Id: DocumentBuilder.java 15607 2008-11-24 15:46:29Z hardy.ferentschik $
package org.hibernate.search.engine;

import org.hibernate.search.ProjectionConstants;

/**
 * Interface created to keep backwards compatibility.
 *
 * @author Hardy Ferentschik
 */
public interface DocumentBuilder {

	/**
	 * Lucene document field name containing the fully qualified classname of the indexed class.
	 *
	 */
	String CLASS_FIELDNAME = ProjectionConstants.OBJECT_CLASS;
}
