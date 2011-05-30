package cz.zcu.kiv.eegdatabase.data.dialect;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 27.5.11
 * Time: 1:13
 * To change this template use File | Settings | File Templates.
 */
public class OracleDialectWithXmlSupport extends Oracle10gDialect {

   public OracleDialectWithXmlSupport() {
      super();
      registerHibernateType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
      registerColumnType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
   }
}
