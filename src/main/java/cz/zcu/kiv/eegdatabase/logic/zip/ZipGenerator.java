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
 *   ZipGenerator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.zip;

/**
 *
 * @author Jan Štěbeták
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.logic.xml.DataTransformer;

public class ZipGenerator implements Generator {

    protected DataTransformer transformer;
    protected String metadata;
    protected String dir;
    protected String dataZip;
    private Log log = LogFactory.getLog(getClass());
    private int fileCounter = 0;

    public File generate(Experiment exp, MetadataCommand mc, Set<DataFile> dataFiles, byte[] licenseFile, String licenseFileName) throws Exception, SQLException, IOException {

        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;
        File tempZipFile = null;

        try {
            log.debug("creating output stream");
            // create temp zip file
            tempZipFile = File.createTempFile("experimentDownload_", ".zip");
            // open stream to temp zip file
            fileOutputStream = new FileOutputStream(tempZipFile);
            // prepare zip stream
            zipOutputStream = new ZipOutputStream(fileOutputStream);

            log.debug("transforming metadata from database to xml file");
            OutputStream meta = getTransformer().transform(exp, mc, dataFiles);
            Scenario scen = exp.getScenario();
            log.debug("getting scenario file");

            byte[] xmlMetadata = null;
            if (meta instanceof ByteArrayOutputStream) {
                xmlMetadata = ((ByteArrayOutputStream) meta).toByteArray();
            }

            ZipEntry entry;
            
            zipOutputStream.putNextEntry(entry = new ZipEntry("License/"+licenseFileName));
            IOUtils.copyLarge(new ByteArrayInputStream(licenseFile), zipOutputStream);
            zipOutputStream.closeEntry();

            if (mc.isScenFile() && scen.getScenarioFile() != null) {
                try {

                    log.debug("saving scenario file (" + scen.getScenarioName() + ") into a zip file");
                    entry = new ZipEntry("Scenario/" + scen.getScenarioName());
                    zipOutputStream.putNextEntry(entry);
                    IOUtils.copyLarge(scen.getScenarioFile().getBinaryStream(), zipOutputStream);
                    zipOutputStream.closeEntry();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (xmlMetadata != null) {
                log.debug("saving xml file of metadata to zip file");
                entry = new ZipEntry(getMetadata() + ".xml");
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(xmlMetadata);
                zipOutputStream.closeEntry();
            }

            for (DataFile dataFile : dataFiles) {
                entry = new ZipEntry(getDataZip() + "/" + dataFile.getFilename());

                if (dataFile.getFileContent().length() > 0) {

                    log.debug("saving data file to zip file");

                    try {

                        zipOutputStream.putNextEntry(entry);

                    } catch (ZipException ex) {

                        String[] partOfName = dataFile.getFilename().split("[.]");
                        String filename;
                        if (partOfName.length < 2) {
                            filename = partOfName[0] + "" + fileCounter;
                        } else {
                            filename = partOfName[0] + "" + fileCounter + "." + partOfName[1];
                        }
                        entry = new ZipEntry(getDataZip() + "/" + filename);
                        zipOutputStream.putNextEntry(entry);
                        fileCounter++;
                    }

                    IOUtils.copyLarge(dataFile.getFileContent().getBinaryStream(), zipOutputStream);
                    zipOutputStream.closeEntry();
                }
            }

            log.debug("returning output stream of zip file");
            return tempZipFile;

        } finally {

            zipOutputStream.flush();
            zipOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            fileCounter = 0;

        }
    }

    /**
     * @return the transformer
     */
    public DataTransformer getTransformer() {
        return transformer;
    }

    /**
     * @param transformer
     *            the transformer to set
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
     * @param metadata
     *            the metadata to set
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
     * @param dataZip
     *            the dataZip to set
     */
    public void setDataZip(String dataZip) {
        this.dataZip = dataZip;
    }

    @Override
    public String toString() {
        return "metadata: " + metadata;
    }
}
