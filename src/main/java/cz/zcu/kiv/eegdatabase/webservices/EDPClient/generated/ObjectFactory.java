
package cz.zcu.kiv.eegdatabase.webservices.EDPClient.generated;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ethz.origo.jerpa.ededclient.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMethodParametersResponse_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "getMethodParametersResponse");
    private final static QName _ProcessDataResponse_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "processDataResponse");
    private final static QName _GetAvailableMethods_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "getAvailableMethods");
    private final static QName _ProcessData_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "processData");
    private final static QName _AvailableProcessingUnitsResponse_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "availableProcessingUnitsResponse");
    private final static QName _GetAvailableMethodsResponse_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "getAvailableMethodsResponse");
    private final static QName _GetMethodParameters_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "getMethodParameters");
    private final static QName _AvailableProcessingUnits_QNAME = new QName("http://webservice.eegprocessor.kiv.zcu.cz/", "availableProcessingUnits");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ethz.origo.jerpa.ededclient.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AvailableProcessingUnits }
     * 
     */
    public AvailableProcessingUnits createAvailableProcessingUnits() {
        return new AvailableProcessingUnits();
    }

    /**
     * Create an instance of {@link GetAvailableMethodsResponse }
     * 
     */
    public GetAvailableMethodsResponse createGetAvailableMethodsResponse() {
        return new GetAvailableMethodsResponse();
    }

    /**
     * Create an instance of {@link GetMethodParameters }
     * 
     */
    public GetMethodParameters createGetMethodParameters() {
        return new GetMethodParameters();
    }

    /**
     * Create an instance of {@link ProcessData }
     * 
     */
    public ProcessData createProcessData() {
        return new ProcessData();
    }

    /**
     * Create an instance of {@link AvailableProcessingUnitsResponse }
     * 
     */
    public AvailableProcessingUnitsResponse createAvailableProcessingUnitsResponse() {
        return new AvailableProcessingUnitsResponse();
    }

    /**
     * Create an instance of {@link GetAvailableMethods }
     * 
     */
    public GetAvailableMethods createGetAvailableMethods() {
        return new GetAvailableMethods();
    }

    /**
     * Create an instance of {@link ProcessDataResponse }
     * 
     */
    public ProcessDataResponse createProcessDataResponse() {
        return new ProcessDataResponse();
    }

    /**
     * Create an instance of {@link GetMethodParametersResponse }
     * 
     */
    public GetMethodParametersResponse createGetMethodParametersResponse() {
        return new GetMethodParametersResponse();
    }

    /**
     * Create an instance of {@link MethodParameters }
     * 
     */
    public MethodParameters createMethodParameters() {
        return new MethodParameters();
    }

    /**
     * Create an instance of {@link DataFile }
     * 
     */
    public DataFile createDataFile() {
        return new DataFile();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetMethodParametersResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "getMethodParametersResponse")
    public JAXBElement<GetMethodParametersResponse> createGetMethodParametersResponse(GetMethodParametersResponse value) {
        return new JAXBElement<GetMethodParametersResponse>(_GetMethodParametersResponse_QNAME, GetMethodParametersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ProcessDataResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "processDataResponse")
    public JAXBElement<ProcessDataResponse> createProcessDataResponse(ProcessDataResponse value) {
        return new JAXBElement<ProcessDataResponse>(_ProcessDataResponse_QNAME, ProcessDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetAvailableMethods }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "getAvailableMethods")
    public JAXBElement<GetAvailableMethods> createGetAvailableMethods(GetAvailableMethods value) {
        return new JAXBElement<GetAvailableMethods>(_GetAvailableMethods_QNAME, GetAvailableMethods.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ProcessData }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "processData")
    public JAXBElement<ProcessData> createProcessData(ProcessData value) {
        return new JAXBElement<ProcessData>(_ProcessData_QNAME, ProcessData.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link AvailableProcessingUnitsResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "availableProcessingUnitsResponse")
    public JAXBElement<AvailableProcessingUnitsResponse> createAvailableProcessingUnitsResponse(AvailableProcessingUnitsResponse value) {
        return new JAXBElement<AvailableProcessingUnitsResponse>(_AvailableProcessingUnitsResponse_QNAME, AvailableProcessingUnitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetAvailableMethodsResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "getAvailableMethodsResponse")
    public JAXBElement<GetAvailableMethodsResponse> createGetAvailableMethodsResponse(GetAvailableMethodsResponse value) {
        return new JAXBElement<GetAvailableMethodsResponse>(_GetAvailableMethodsResponse_QNAME, GetAvailableMethodsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link GetMethodParameters }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "getMethodParameters")
    public JAXBElement<GetMethodParameters> createGetMethodParameters(GetMethodParameters value) {
        return new JAXBElement<GetMethodParameters>(_GetMethodParameters_QNAME, GetMethodParameters.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link AvailableProcessingUnits }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.eegprocessor.kiv.zcu.cz/", name = "availableProcessingUnits")
    public JAXBElement<AvailableProcessingUnits> createAvailableProcessingUnits(AvailableProcessingUnits value) {
        return new JAXBElement<AvailableProcessingUnits>(_AvailableProcessingUnits_QNAME, AvailableProcessingUnits.class, null, value);
    }

}
