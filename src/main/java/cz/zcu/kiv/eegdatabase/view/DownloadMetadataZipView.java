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
 *   DownloadMetadataZipView.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.view;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.zip.Generator;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;
/**
 *
 * @author Jan Štěbeták
 */
public class DownloadMetadataZipView extends AbstractView{

  private Generator zipGenerator;

  @Override
  protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
      Experiment meas = (Experiment) map.get("dataObject");
      List<Experiment> meases = new ArrayList<Experiment>();
      meases.add(meas);
//      OutputStream out = getZipGenerator().generate(meases, true);
//      ByteArrayOutputStream bout = null;
//      response.setHeader("Content-Type", "application/zip");
//      if (out instanceof ByteArrayOutputStream) {
//          bout = (ByteArrayOutputStream) out;
//          response.getOutputStream().write(bout.toByteArray());
//      }
  }

  /**
   * @return the zipGenerator
   */
  public Generator getZipGenerator() {
    return zipGenerator;
  }

  /**
   * @param zipGenerator the zipGenerator to set
   */
  public void setZipGenerator(Generator zipGenerator) {
    this.zipGenerator = zipGenerator;
  }


}
