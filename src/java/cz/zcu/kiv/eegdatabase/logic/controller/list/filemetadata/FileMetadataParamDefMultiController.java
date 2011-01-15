package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 */
public class FileMetadataParamDefMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing file metadata params list controller");
        ModelAndView mav = new ModelAndView("lists/fileMetadataParams/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<FileMetadataParamDef> list = fileMetadataParamDefDao.getItemsForList();
        mav.addObject("fileMetadataParamsList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting metadata parameter definition.");
        ModelAndView mav = new ModelAndView("/lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (fileMetadataParamDefDao.canDelete(id)) {
                fileMetadataParamDefDao.delete(fileMetadataParamDefDao.read(id));
            } else {
                mav.setViewName("/lists/itemUsed");
            }
        }

        return mav;
    }
}
