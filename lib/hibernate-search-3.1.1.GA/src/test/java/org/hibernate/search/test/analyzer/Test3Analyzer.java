//$Id: Test3Analyzer.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.test.analyzer;

/**
 * @author Emmanuel Bernard
 */
public class Test3Analyzer extends AbstractTestAnalyzer {
	private final String[] tokens = { "music", "elephant", "energy" };

	protected String[] getTokens() {
		return tokens;
	}
}
