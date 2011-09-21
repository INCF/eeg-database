BEGIN
dbms_xmlschema.registerschema(
    SCHEMAURL => 'scenarios.xsd',
    SCHEMADOC => '<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdb="http://xmlns.oracle.com/xdb" xmlns="urn:x-eegdatabase:schema:scenarios.1.0" targetNamespace="urn:x-eegdatabase:schema:scenarios.1.0" elementFormDefault="qualified" xdb:storeVarrayAsTable="true">
	<xs:simpleType name="nameType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="srcType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:complexType name="scenarioMetaType" xdb:SQLType="MYSCENARIO_META_TYPE">
		<xs:attribute name="name" type="nameType" use="required" xdb:SQLName="NAME"/>
		<xs:attribute name="src" type="srcType" use="required" xdb:SQLName="SRC"/>
	</xs:complexType>
	<xs:complexType name="scenariosType" xdb:SQLType="MYSCENARIOS_TYPE">
		<xs:sequence>
			<xs:element name="scenario" type="scenarioMetaType" minOccurs="1" maxOccurs="unbounded" xdb:SQLName="ScenarioMeta"/>
		</xs:sequence>
	</xs:complexType>
	<xs:element name="scenarios" type="scenariosType" xdb:SQLName="Scenarios" xdb:defaultTable="SCENARIOS_XML">
		<xs:annotation>
			<xs:documentation>Root element</xs:documentation>
		</xs:annotation>
	</xs:element>
</xs:schema>',
    LOCAL       => TRUE,
    GENTYPES    => TRUE,
    GENBEAN     => FALSE,
    GENTABLES   => TRUE,
    FORCE       => FALSE,
    OWNER       => USER
  );
  END;