/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.zip;

/**
 *
 * @author Jan Štěbeták
 */

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.IScenarioType;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.logic.xml.DataTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class ZipGenerator implements Generator {

    protected DataTransformer transformer;
    protected String metadata;
    protected String dir;
    protected String dataZip;
    private Log log = LogFactory.getLog(getClass());
    private int fileCounter = 0;

    public OutputStream generate(Experiment exp, MetadataCommand mc, Set<DataFile> dataFiles) throws Exception, SQLException, IOException {
        ZipOutputStream zos = null;
        ByteArrayOutputStream baos = null;
        try {
            log.debug("creating output stream");
            baos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(baos);

            log.debug("transforming metadata from database to xml file");
            OutputStream meta = getTransformer().transform(exp, mc, dataFiles);
            Scenario scen = exp.getScenario();
            log.debug("getting scenario file");

            IScenarioType scenFile = scen.getScenarioType();

            byte[] xmlMetadata = null;
            if (meta instanceof ByteArrayOutputStream) {
                xmlMetadata = ((ByteArrayOutputStream) meta).toByteArray();
            }
            byte[] scenario;
            ZipEntry e;

            if (mc.isScenFile()) {
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

            for (DataFile d : dataFiles) {
                e = new ZipEntry(getDataZip() + "/" + d.getFilename());
                Blob blob = d.getFileContent();
                if (blob != null) {
                    byte[] pole = blob.getBytes(1, (int) blob.length());
                    log.debug("saving data file to zip file");
                    try {
                        zos.putNextEntry(e);
                    } catch (ZipException ex) {
                        String[] partOfName = d.getFilename().split("[.]");
                        String filename;
                        if (partOfName.length < 2) {
                            filename = partOfName[0] + "" + fileCounter;
                        } else {
                            filename = partOfName[0] + "" + fileCounter + "." + partOfName[1];
                        }
                        e = new ZipEntry(getDataZip() + "/" + filename);
                        zos.putNextEntry(e);
                        fileCounter++;
                    }

                    zos.write(pole);
                    zos.closeEntry();
                }
            }
            log.debug("returning output stream of zip file");

        } finally {
            zos.flush();
            zos.close();
            fileCounter = 0;

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

