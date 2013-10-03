/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   OracleXMLType.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.datatypes;

import oracle.jdbc.OracleResultSet;
import oracle.sql.OPAQUE;
import oracle.xdb.XMLType;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Koren
 */
public class OracleXMLType implements UserType, Serializable {

    private static final long serialVersionUID = 2308230823023l;
    private static final Class returnedClass = Document.class;
    private static final int[] SQL_TYPES = new int[]{oracle.xdb.XMLType._SQL_TYPECODE};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return returnedClass;
    }

    public int hashCode(Object _obj) {
        return _obj.hashCode();
    }

    public Object assemble(Serializable _cached, Object _owner)
            throws HibernateException {
        try {
            return OracleXMLType.stringToDom((String) _cached);
        } catch (Exception e) {
            throw new HibernateException(
                    "Could not assemble String to Document", e);
        }
    }

    public Serializable disassemble(Object _obj) throws HibernateException {
        try {
            return OracleXMLType.domToString((Document) _obj);
        } catch (Exception e) {
            throw new HibernateException(
                    "Could not disassemble Document to Serializable", e);
        }
    }

    public Object replace(Object _orig, Object _tar, Object _owner) {
        return deepCopy(_orig);
    }

    public boolean equals(Object arg0, Object arg1) throws HibernateException {
        if (arg0 == null && arg1 == null)
            return true;
        else if (arg0 == null && arg1 != null)
            return false;
        else
            return arg0.equals(arg1);
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object arg2)
            throws HibernateException, SQLException {
        XMLType xmlType = null;
        Document doc = null;
        try {
            OPAQUE op = null;
            OracleResultSet ors = null;
            if (rs instanceof OracleResultSet) {
                ors = (OracleResultSet) rs;
            } else {
                throw new UnsupportedOperationException("ResultSet needs to be of type OracleResultSet");
            }
            op = ors.getOPAQUE(names[0]);
            if (op != null) {
               //xmlType = XMLType.createXML(op);
            }
            if(xmlType != null) {
            doc = xmlType.getDocument();
            }
            else {
                doc = null;
            }
        } finally {
            if (null != xmlType) {
                xmlType.close();
            }
        }
        return doc;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
            throws HibernateException, SQLException {

        try {
            XMLType xmlType = null;
            if (value != null) {
                String xml = OracleXMLType.domToString((Document) value);
                xmlType = XMLType.createXML(st.getConnection(), xml);
            }
            st.setObject(index, xmlType);
        } catch (Exception e) {
            throw new SQLException("Could not convert Document to String for storage", e);
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null)
            return null;

        return (Document) ((Document) value).cloneNode(true);
    }

    public boolean isMutable() {
        return false;
    }

    protected static String domToString(Document doc)
            throws TransformerException, IOException {

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        writer.flush();

        return writer.toString();
    }

    protected static Document stringToDom(String xmlSource)
            throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlSource.getBytes("UTF - 8")));
    }

}
