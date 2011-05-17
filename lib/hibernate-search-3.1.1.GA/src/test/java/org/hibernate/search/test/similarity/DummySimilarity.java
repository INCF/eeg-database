// $Id: DummySimilarity.java 16554 2009-05-13 12:38:19Z hardy.ferentschik $
package org.hibernate.search.test.similarity;

import org.apache.lucene.search.DefaultSimilarity;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings("serial")
public class DummySimilarity extends DefaultSimilarity {
	private float CONST = 1.0f;

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
