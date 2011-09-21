BEGIN
dbms_xmlschema.registerschema(
    SCHEMAURL => 'subjects.xsd',
    SCHEMADOC => '<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdb="http://xmlns.oracle.com/xdb" xmlns="urn:x-eegdatabase:schema:subjects.1.0" targetNamespace="urn:x-eegdatabase:schema:subjects.1.0" elementFormDefault="qualified" xdb:storeVarrayAsTable="true">
	<xs:simpleType name="firstnameType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="lastnameType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="nameType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="resultType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="testType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:simpleType name="dateType">
		<xs:restriction base="xs:string"/>
	</xs:simpleType>
	<xs:complexType name="testInfoType" xdb:SQLType="TEST_INFO_TYPE">
		<xs:sequence minOccurs="1" maxOccurs="1">
			<xs:element name="name" type="nameType" xdb:SQLName="NAME"/>
			<xs:element name="result" type="resultType" xdb:SQLName="RESULT"/>
			<xs:element name="test" type="testType" xdb:SQLName="TEST"/>
			<xs:element name="date" type="dateType" xdb:SQLName="DATE"/>
		</xs:sequence>
	</xs:complexType>
	<xs:complexType name="testsType" xdb:SQLType="TESTS_TYPE">
		<xs:sequence>
			<xs:element name="test" type="testInfoType" minOccurs="1" maxOccurs="unbounded" xdb:SQLName="TEST_INFO"/>
		</xs:sequence>
	</xs:complexType>
	<xs:complexType name="subjectType" xdb:SQLType="SUBJECT_TYPE">
		<xs:sequence minOccurs="1" maxOccurs="1">
			<xs:element name="firstname" type="firstnameType" xdb:SQLName="FIRST_NAME"/>
			<xs:element name="lastname" type="lastnameType" xdb:SQLName="LAST_NAME"/>
			<xs:element name="tests" type="testsType" xdb:SQLName="TESTS"/>
		</xs:sequence>
	</xs:complexType>
	<xs:complexType name="subjectsType" xdb:SQLType="SUBJECTS_TYPE">
		<xs:sequence>
			<xs:element name="subject" type="subjectType" minOccurs="1" maxOccurs="unbounded" xdb:SQLName="SUBJECT"/>
		</xs:sequence>
	</xs:complexType>
	<xs:element name="subjects" type="subjectsType" xdb:defaultTable="SUBJECTS_XML">
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