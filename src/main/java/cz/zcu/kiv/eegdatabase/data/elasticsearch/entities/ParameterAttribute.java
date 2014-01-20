package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

/**
 *
 * @author bydga
 */
public class ParameterAttribute {

	private String name;
	private String value;

	public ParameterAttribute() {
	}

	public ParameterAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}