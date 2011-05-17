// $Id: BufferSharingReaderPerfTest.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.test.reader.performance;

import org.hibernate.search.reader.SharingBufferReaderProvider;

/**
 * @author Sanne Grinovero
 */
public class BufferSharingReaderPerfTest extends ReaderPerformance {

	@Override
	protected String getReaderStrategyName() {
		return SharingBufferReaderProvider.class.getName();
	}

}
