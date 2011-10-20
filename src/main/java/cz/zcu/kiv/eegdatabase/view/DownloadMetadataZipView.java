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
