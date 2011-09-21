BEGIN
dbms_xmlschema.registerschema(
    SCHEMAURL => 'p300.xsd',
    SCHEMADOC => '<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdb="http://xmlns.oracle.com/xdb" xmlns="urn:x-eegdatabase:schema:p300.1.0" targetNamespace="urn:x-eegdatabase:schema:p300.1.0" elementFormDefault="qualified" xdb:storeVarrayAsTable="true">
	<xs:simpleType name="srcType">
		<xs:restriction base="xs:anyURI"/>
	</xs:simpleType>
	<xs:simpleType name="typeType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="instructionType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:complexType name="phaseType" xdb:SQLType="PHASE_TYPE">
		<xs:attribute name="src" type="srcType" use="required" xdb:SQLName="SRC"/>
		<xs:attribute name="type" type="typeType" use="required" xdb:SQLName="TYPE"/>
		<xs:attribute name="instruction" type="instructionType" use="required" xdb:SQLName="INSTRUCTION"/>
	</xs:complexType>
	<xs:complexType name="scenarioType" xdb:SQLType="SCENARIO_TYPE">
		<xs:sequence>
			<xs:element name="phase" type="phaseType" minOccurs="1" maxOccurs="unbounded" xdb:SQLName="PHASE"/>
		</xs:sequence>
	</xs:complexType>
	<xs:element name="scenario" type="scenarioType" xdb:defaultTable="SCENARIO_XML">
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