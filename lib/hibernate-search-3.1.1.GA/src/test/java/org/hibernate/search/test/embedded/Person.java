// $Id: Person.java 16554 2009-05-13 12:38:19Z hardy.ferentschik $
package org.hibernate.search.test.embedded;

/**
 * @author Emmanuel Bernard
 */
public interface Person {
	public String getName();

	public void setName(String name);

	public Address getAddress();

	public void setAddress(Address address);
}
