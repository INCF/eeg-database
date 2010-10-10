/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleGenericDao;
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
public class DailyHistoryController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private SimpleHistoryDao<History, Integer> historyDao;

  public DailyHistoryController() {
    //Initialize controller properties here or
    //in the Web Application Context
    //setCommandClass(MyCommand.class);
    //setCommandName("MyCommandName");
    //setSuccessView("successView");
    //setFormView("formView");
    }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
    log.debug("Processing daily download history");
    String countOfDownloadedFiles;
    List<History> historyList = null;
    List<History> lastDownloadedFilesHistoryList = null;
    List<DownloadStatistic> topDownloadedFilesList = null;
    ModelAndView mav = new ModelAndView("history/dailyHistory");
    historyList = historyDao.getDailyHistory();
    lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory();
    topDownloadedFilesList = historyDao.getDailyTopDownloadHistory();

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
