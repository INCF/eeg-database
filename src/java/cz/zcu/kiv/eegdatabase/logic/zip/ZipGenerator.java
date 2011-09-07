/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.zip;

/**
 *
 * @author Jan Štěbeták
 */

import java.io.*;
import java.sql.Blob;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.IScenarioType;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;

import java.sql.SQLException;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.zcu.kiv.eegdatabase.logic.xml.DataTransformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class ZipGenerator implements Generator {

    protected DataTransformer transformer;
    protected String metadata;
    protected String dir;
    protected String dataZip;
    private Log log = LogFactory.getLog(getClass());

    public OutputStream generate(Experiment exp, boolean isScenName) throws Exception, SQLException, IOException {
        ZipOutputStream zos = null;
        ByteArrayOutputStream baos = null;
        try {
            log.debug("creating output stream");
            baos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(baos);

            log.debug("getting datas from experiment object");
            Set<DataFile> datas = exp.getDataFiles();
            log.debug("transforming metadata from database to xml file");
            OutputStream meta = getTransformer().transform(exp, isScenName);
            Scenario scen = exp.getScenario();
            log.debug("getting scenario file");

            IScenarioType scenFile = scen.getScenarioType();

            byte[] xmlMetadata = null;
            if (meta instanceof ByteArrayOutputStream) {
                xmlMetadata = ((ByteArrayOutputStream) meta).toByteArray();
            }
            byte[] scenario;
            ZipEntry e;

            if (scenFile != null) {
                try {
                    scenario = toByteArray(scenFile.getScenarioXml());
                    log.debug("saving scenario file (" + scen.getScenarioName() + ") into a zip file");
                    e = new ZipEntry("Scenario/" + scen.getScenarioName());
                    zos.putNextEntry(e);
                    zos.write(scenario);
                    zos.closeEntry();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (xmlMetadata != null) {
                log.debug("saving xml file of metadata to zip file");
                e = new ZipEntry(getMetadata() + ".xml");
                zos.putNextEntry(e);
                zos.write(xmlMetadata);
                zos.closeEntry();
            }

            for (DataFile d : datas) {
                e = new ZipEntry(getDataZip() + "/" + d.getFilename());
                Blob blob = d.getFileContent();
                if (blob != null) {
                    byte[] pole = blob.getBytes(1, (int) blob.length());
                    log.debug("saving data file to zip file");
                    zos.putNextEntry(e);
                    zos.write(pole);
                    zos.closeEntry();
                }
            }
            log.debug("returning output stream of zip file");

        } finally {
            zos.flush();
            zos.close();

        }
        return baos;
    }

    private byte[] toByteArray(Object o) throws Exception {
        if (o instanceof Blob) {
            return ((Blob) o).getBytes(1, (int) ((Blob) o).length());
        } else if (o instanceof Document) {
            Source source = new DOMSource((Document) o);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);

            return out.toByteArray();
        }

        return null;
    }

    /**
     * @return the transformer
     */
    public DataTransformer getTransformer() {
        return transformer;
    }

    /**
     * @param transformer the transformer to set
     */
    public void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }

    /**
     * @return the metadata
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the dataZip
     */
    public String getDataZip() {
        return dataZip;
    }

    /**
     * @param dataZip the dataZip to set
     */
    public void setDataZip(String dataZip) {
        this.dataZip = dataZip;
    }

    @Override
    public String toString() {
        return "metadata: " + metadata;
    }
}

