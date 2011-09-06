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
import java.io.OutputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.bind.JAXBException;
import cz.zcu.kiv.eegdatabase.logic.xml.DataTransformer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZipGenerator implements Generator {

  protected DataTransformer transformer;
  protected String metadata;
  protected String dir;
  protected String dataZip;
  private Log log = LogFactory.getLog(getClass());

  public OutputStream generate(Experiment exp, boolean isScenName) throws JAXBException, SQLException, IOException {
//    ZipOutputStream zos = null;
//    ByteArrayOutputStream baos = null;
//    Map<String, Integer> fileNamesAndCounts = new HashMap<String, Integer>();
//    try {
//      log.debug("creating output stream");
//      baos = new ByteArrayOutputStream();
//      zos = new ZipOutputStream(baos);
//      int count = 1;
//
//      for (Experiment meas : meases) {
//        String directory = getDir() + count + File.separator;
//        log.debug("getting datas from measuration object");
//        Set<DataFile> datas = meas.getDataFiles();
//        log.debug("transforming metadata from database to xml file");
//        OutputStream meta = getTransformer().transform(meas, isScenName);
//        Scenario scen = meas.getScenario();
//        log.debug("getting scenario file");
//        Blob scenarioXml = (Blob) scen.getScenarioType().getScenarioXml();
//
//        byte[] xmlMetadata = null;
//        if (meta instanceof ByteArrayOutputStream) {
//          xmlMetadata = ((ByteArrayOutputStream) meta).toByteArray();
//        }
//        byte[] scenario = null;
//        log.debug("creating experiment directory in zip file: " + directory);
//        ZipEntry e = new ZipEntry(directory);
//        zos.putNextEntry(e);
//
//        zos.closeEntry();
//        if (scenarioXml != null) {
//          scenario = scenarioXml.getBytes(1, (int) scenarioXml.length());
//          log.debug("saving xml file of scenario (" + scen.getTitle() + ".xml) to zip file");
//          e = new ZipEntry(directory + scen.getTitle() + ".xml");
//          zos.putNextEntry(e);
//          zos.write(scenario);
//          zos.closeEntry();
//        }
//        if (xmlMetadata != null) {
//          log.debug("saving xml file of metadata to zip file");
//          e = new ZipEntry(directory + getMetadata() + ".xml");
//          zos.putNextEntry(e);
//          zos.write(xmlMetadata);
//          zos.closeEntry();
//        }
//
//        String dataDir = getDataZip() + File.separator;
////        ZipEntry zipDir = new ZipEntry(directory + dataDir);
////        zos.putNextEntry(zipDir);
////        zos.closeEntry();
//        log.debug("creating directory for data files: " + dataDir);
//        if (datas != null) {
//          if (datas.size() > 0) {
//            for (DataFile data : datas) {
//              Blob blob = data.getFileContent();
//              if (blob != null) {
//                byte[] pole = blob.getBytes(1, (int) blob.length());
//                log.debug("saving data file to zip file");
//                String fileName = data.getFilename();
//                String newName = "";
//                if (fileNamesAndCounts.containsKey(fileName)) {
//                  String[] partOfName = fileName.split("[.]");
//                  if (partOfName.length < 2) {
//                   newName = partOfName[0]+""+fileNamesAndCounts.get(fileName);
//                   fileNamesAndCounts.put(fileName, (fileNamesAndCounts.get(fileName))+1);
//                  }
//                  else {
//                   for (int i = 0; i < partOfName.length-2; i++) {
//                     newName = partOfName[i]+".";
//                   }
//                   newName = newName + partOfName[partOfName.length-2]+fileNamesAndCounts.get
//                           (fileName)+"."+partOfName[partOfName.length-1];
//                   fileNamesAndCounts.put(fileName, (fileNamesAndCounts.get(fileName))+1);
//                  }
//
//                }
//                else {
//                  newName = fileName;
//                  fileNamesAndCounts.put(fileName, 0);
//                }
//                ZipEntry z = new ZipEntry(directory + dataDir + newName);
//                zos.putNextEntry(z);
//                zos.write(pole);
//                zos.closeEntry();
//              }
//            }
//          }
//        }
//
//        count++;
//      }
//
//      log.debug("returning output stream of zip file");
//
//    } finally {
//      fileNamesAndCounts.clear();
//      zos.flush();
//      zos.close();
//      return baos;
//    }
      return getTransformer().transform(exp, isScenName);
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
   * @return the dir
   */
  public String getDir() {
    return dir;
  }

  /**
   * @param dir the dir to set
   */
  public void setDir(String dir) {
    this.dir = dir;
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

