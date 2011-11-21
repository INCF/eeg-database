package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("addHardware")
public class AddHardwareController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    private final int DEFAULT_ID = -1;
    AddHardwareValidator addHardwareValidator;

    @Autowired
	public AddHardwareController(AddHardwareValidator addHardwareValidator){
		this.addHardwareValidator = addHardwareValidator;
	}

    @RequestMapping(value="lists/hardware-definitions/edit.html", method=RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, ModelMap model){
        AddHardwareCommand data = new AddHardwareCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);

            if (idString != null) {
                // Editation of existing hardware
                int id = Integer.parseInt(idString);

                log.debug("Loading hardware to the command object for editing.");
                Hardware hardware = hardwareDao.read(id);

                data.setId(id);
                data.setDescription(hardware.getDescription());
                data.setTitle(hardware.getTitle());
                data.setType(hardware.getType());
            }
            model.addAttribute("addHardware",data);

            return "lists/hardware/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/hardware-definitions/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model) throws Exception {
        AddHardwareCommand data = new AddHardwareCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addHardware",data);
            return "lists/hardware/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addHardware") AddHardwareCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addHardwareValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/hardware/addItemForm";
            }

            Hardware hardware;
            if (data.getId() > 0) {
                // Editing
                log.debug("Editing existing hardware object.");
                hardware = hardwareDao.read(data.getId());
                hardware.setDescription(data.getDescription());
                hardware.setTitle(data.getTitle());
                hardware.setType(data.getType());
                hardwareDao.update(hardware);
            } else {
                // Creating new
                hardware = new Hardware();
                hardware.setTitle(data.getTitle());
                hardware.setType(data.getType());
                hardware.setDescription(data.getDescription());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default hardware object.");
                    hardwareDao.createDefaultRecord(hardware);
                }else{
                    log.debug("Creating new group hardware object.");
                    int pkHardware = hardwareDao.create(hardware);

                    HardwareGroupRelId hardwareGroupRelId = new HardwareGroupRelId(pkHardware,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    HardwareGroupRel hardwareGroupRel = new HardwareGroupRel(hardwareGroupRelId,researchGroup,hardware);
                    hardwareDao.createGroupRel(hardwareGroupRel);
                }

            }
            log.debug("Returning MAV");
            return "redirect:list.html";
        }else{
            return "lists/userNotExperimenter";
        }
    }
}
