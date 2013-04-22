
package cz.zcu.kiv.eegdatabase.webservices.processor.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for supportedFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="supportedFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="KIV_FORMAT"/>
 *     &lt;enumeration value="DOUBLE_FORMAT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "supportedFormat")
@XmlEnum
public enum SupportedFormat {

    KIV_FORMAT,
    DOUBLE_FORMAT;

    public String value() {
        return name();
    }

    public static SupportedFormat fromValue(String v) {
        return valueOf(v);
    }

}
