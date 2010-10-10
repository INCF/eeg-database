// $Id: IncrementalOptimizerStrategyTest.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.test.optimizer;

/**
 * @author Emmanuel Bernard
 */
public class IncrementalOptimizerStrategyTest extends OptimizerTestCase {
	protected void configure(org.hibernate.cfg.Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( "hibernate.search.default.optimizer.transaction_limit.max", "10" );
	}
}
