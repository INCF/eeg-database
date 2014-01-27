package cz.zcu.kiv.eegdatabase.webservices.rest.common.utils;



import java.sql.Blob;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.hibernate.Hibernate;

public class BlobSerializer extends XmlAdapter<String, Blob> {

	@Override
	public Blob unmarshal(String v) throws Exception {
		if (v == null) {
			v = "";
		}
		return Hibernate.createBlob(v.getBytes());
	}

	@Override
	public String marshal(Blob v) throws Exception {
		if (v == null) {
			return "";
		}
		return v.getBytes(1l, (int) v.length()).toString();
	}
}