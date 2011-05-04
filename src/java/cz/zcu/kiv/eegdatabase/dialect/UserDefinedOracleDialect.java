package cz.zcu.kiv.eegdatabase.dialect;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 27.4.11
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public class UserDefinedOracleDialect extends Oracle10gDialect {
    public UserDefinedOracleDialect() {
        super();
        registerHibernateType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
        registerColumnType(oracle.xdb.XMLType._SQL_TYPECODE, "xmltype");
    }
}
