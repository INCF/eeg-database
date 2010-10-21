/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleHistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author pbruha
 */
public class MonthlyHistoryController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private SimpleHistoryDao<History, Integer> historyDao;

  public MonthlyHistoryController() {
  }

  protected ModelAndView handleRequestInternal(
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
    log.debug("Processing monthly download history");
    ModelAndView mav = new ModelAndView("history/monthlyHistory");
    String countOfDownloadedFiles;
    List<History> historyList = null;
    String historyType="monthly";
    List<History> lastDownloadedFilesHistoryList = null;
    List<DownloadStatistic> topDownloadedFilesList = null;
    historyList = historyDao.getHistory(historyType);
    lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory(historyType);
    topDownloadedFilesList = historyDao.getTopDownloadHistory(historyType);

    countOfDownloadedFiles = "" + historyList.size();
    mav.addObject("countOfDownloadedFiles", countOfDownloadedFiles);
    mav.addObject("historyList", historyList);
    mav.addObject("topDownloadedFilesList", topDownloadedFilesList);
    mav.addObject("lastDownloadedFilesHistoryList", lastDownloadedFilesHistoryList);
    return mav;
  }

  public SimpleHistoryDao<History, Integer> getHistoryDao() {
    return historyDao;
  }

  public void setHistoryDao(SimpleHistoryDao<History, Integer> historyDao) {
    this.historyDao = historyDao;
  }
}
