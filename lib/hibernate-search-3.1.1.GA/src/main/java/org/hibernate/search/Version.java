//$Id: Version.java 16629 2009-05-28 15:03:17Z epbernard $
package org.hibernate.search;

import org.hibernate.search.util.LoggerFactory;

/**
 * @author Emmanuel Bernard
 */
public class Version {
	
	public static final String VERSION = "3.1.1.GA";

	static {
		LoggerFactory.make().info( "Hibernate Search {}", VERSION );
	}

	public static void touch() {
	}
	
}
