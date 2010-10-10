package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author JiPER
 */
public class FileMetadataParamListController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<FileMetadataParamDef, Integer> fileMetadataParamDefDao;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Processing file metadata params list controller");
    ModelAndView mav = new ModelAndView("lists/fileMetadataParams/list");

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());

    List<FileMetadataParamDef> list = fileMetadataParamDefDao.getAllRecords();
    mav.addObject("fileMetadataParamsList", list);
    return mav;
  }

  public GenericDao<FileMetadataParamDef, Integer> getFileMetadataParamDefDao() {
    return fileMetadataParamDefDao;
  }

  public void setFileMetadataParamDefDao(GenericDao<FileMetadataParamDef, Integer> fileMetadataParamDefDao) {
    this.fileMetadataParamDefDao = fileMetadataParamDefDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
