/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleHistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.PublicCloneable;
import org.jfree.util.Rotation;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author pbruha
 */
public class GraphController extends AbstractController {

  public GraphController() {
  }
  private Log log = LogFactory.getLog(getClass());
  private SimpleHistoryDao<History, Integer> historyDao;

  protected ModelAndView handleRequestInternal(
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
    log.debug("Processing creating graph");
    String graphType = "";
    graphType = request.getParameter("graphType");
    List<DownloadStatistic> topDownloadedFilesList = null;

    long countFile = 0;
    int topFiles = 5;
    response.setContentType("image/png");
    topDownloadedFilesList = historyDao.getTopDownloadHistory(graphType);


    DefaultPieDataset dataset = new DefaultPieDataset();
    for (int i = 0; i < topDownloadedFilesList.size(); i++) {
      dataset.setValue(topDownloadedFilesList.get(i).getFileType(), new Long(topDownloadedFilesList.get(i).getCount()));
      countFile = countFile + topDownloadedFilesList.get(i).getCount();
    }
    if (historyDao.getCountOfFilesHistory(graphType) > countFile) {
      dataset.setValue("Other", historyDao.getCountOfFilesHistory(graphType) - countFile);
    }
    JFreeChart chart = ChartFactory.createPieChart3D(
            "Daily downloads", // chart title
            dataset, // data
            true, // include legend
            true,
            false);

    PiePlot3D plot = (PiePlot3D) chart.getPlot();
    plot.setStartAngle(290);
    plot.setDirection(Rotation.CLOCKWISE);
    plot.setForegroundAlpha(0.5f);
    plot.setNoDataMessage("No data to display");

//    JFreeChart chart = ChartFactory.createPieChart("Daily downloads",
//            dataset,
//            true, // legend?
//            true, // tooltips?
//            false // URLs?
//            );
//    // Assume that we have the chart
    ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 600, 400);
    response.getOutputStream().close();
    return null;
  }

  public SimpleHistoryDao<History, Integer> getHistoryDao() {
    return historyDao;
  }

  public void setHistoryDao(SimpleHistoryDao<History, Integer> historyDao) {
    this.historyDao = historyDao;
  }
}
