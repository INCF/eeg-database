//$Id: MDBSearchController.java 16554 2009-05-13 12:38:19Z hardy.ferentschik $
package org.hibernate.search.test.jms.master;

import javax.jms.MessageListener;

import org.hibernate.search.backend.impl.jms.AbstractJMSHibernateSearchController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Emmanuel Bernard
 */
public class MDBSearchController extends AbstractJMSHibernateSearchController {

	SessionFactory sessionFactory;

	MDBSearchController( SessionFactory sessionFactory ) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.openSession( );
	}

	protected void cleanSessionIfNeeded(Session session) {
		session.close();
	}
}
