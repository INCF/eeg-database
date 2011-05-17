// $Id: SharedReaderPerfTest.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.test.reader.performance;

/**
 * @author Sanne Grinovero
 */
public class SharedReaderPerfTest extends ReaderPerformance {

	@Override
	protected String getReaderStrategyName() {
		return "shared";
	}

}
