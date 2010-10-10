//$Id: Resolution.java 15624 2008-11-27 13:12:23Z hardy.ferentschik $
package org.hibernate.search.annotations;

/**
 * Date indexing resolution.
 *
 * @author Emmanuel Bernard
 */
public enum Resolution {
	YEAR,
	MONTH,
	DAY,
	HOUR,
	MINUTE,
	SECOND,
	MILLISECOND
}
