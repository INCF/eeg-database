package cz.zcu.kiv.eegdatabase.logic.controller.list.artifact;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Honza
 * Date: 24.6.12
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes("addArtifact")
public class AddArtifactController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private GenericListDao<Artifact> artifactDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    AddArtifactValidator addArtifactValidator;

    @Autowired
    public AddArtifactController(AddArtifactValidator addArtifactValidator) {
        this.addArtifactValidator = addArtifactValidator;
    }

    @RequestMapping(value = "lists/artifact-definitions/edit.html", method = RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request) {
        AddArtifactCommand data = new AddArtifactCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                String title = researchGroupDao.getResearchGroupTitle(id);
                data.setResearchGroupTitle(title);


                // Editing of existing artifact
                id = Integer.parseInt(idString);

                log.debug("Loading artifact to the command object for editing.");
                Artifact artifact = artifactDao.read(id);
                data.setId(id);
                data.setCompensation(artifact.getCompensation());
                data.setRejectCondition(artifact.getRejectCondition());

            model.addAttribute("addArtifact", data);

            return "lists/artifact/addItemForm";
        } else {
            return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value = "lists/artifact-definitions/add.html", method = RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddArtifactCommand data = new AddArtifactCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                data.setResearchGroupId(id);

                String title = researchGroupDao.getResearchGroupTitle(id);
                data.setResearchGroupTitle(title);

            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addArtifact", data);
            return "lists/artifact/addItemForm";
        } else {
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addArtifact") AddArtifactCommand data, BindingResult result, ModelMap model) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addArtifactValidator.validate(data, result);
            if (result.hasErrors()) {
                return "lists/artifact/addItemForm";
            }
            int artifactId = data.getId();
            Artifact artifact;
            if (artifactId > 0) {
                // Editing
                log.debug("Editing existing artifact object.");
                artifact = artifactDao.read(artifactId);
                artifact.setCompensation(data.getCompensation());
                artifact.setRejectCondition(data.getRejectCondition());

                artifactDao.update(artifact);
            } else {
                Artifact newArtifact = new Artifact();
                ResearchGroup group = researchGroupDao.read(data.getResearchGroupId());
                newArtifact.setCompensation(data.getCompensation());
                newArtifact.setRejectCondition(data.getRejectCondition());
                Set<ResearchGroup> groups = new HashSet<ResearchGroup>(0);
                groups.add(group);
                newArtifact.setResearchGroups(groups);
                artifactDao.create(newArtifact);

            }
            log.debug("Returning MAV");
            String redirect = "redirect:list.html?groupid=" + data.getResearchGroupId();
            return redirect;
        } else {
            return "lists/userNotExperimenter";
        }
    }
}
