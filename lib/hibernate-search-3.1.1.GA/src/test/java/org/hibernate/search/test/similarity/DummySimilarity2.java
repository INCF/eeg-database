// $Id: DummySimilarity2.java 14954 2008-07-17 20:43:10Z sannegrinovero $
package org.hibernate.search.test.similarity;

import org.apache.lucene.search.DefaultSimilarity;

/**
 * @author Emmanuel Bernard
 */
public class DummySimilarity2 extends DefaultSimilarity {
	private float CONST = .5f;

	@Override
	public float lengthNorm(String fieldName, int numTerms) {
		return CONST;
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		return CONST;
	}

	@Override
	public float tf(float freq) {
		return CONST;
	}

	@Override
	public float sloppyFreq(int distance) {
		return CONST;
	}

	@Override
	public float idf(int docFreq, int numDocs) {
		return CONST;
	}

	@Override
	public float coord(int overlap, int maxOverlap) {
		return CONST;
	}
}