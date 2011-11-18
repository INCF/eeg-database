package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
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

    AddHardwareValidator addHardwareValidator;

    @Autowired
	public AddHardwareController(AddHardwareValidator addHardwareValidator){
		this.addHardwareValidator = addHardwareValidator;
	}

    @RequestMapping(value="lists/hardware-definitions/edit.html", method=RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, ModelMap model){
        AddHardwareCommand data = new AddHardwareCommand();
        if (!auth.userIsExperimenter()) {
            return "lists/userNotExperimenter";
        }
        model.addAttribute("userIsExperimenter", auth.userIsExperimenter());

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
    }

    @RequestMapping(value="lists/hardware-definitions/add.html",method=RequestMethod.GET)
    protected String showAddForm(ModelMap model) throws Exception {
        AddHardwareCommand data = new AddHardwareCommand();
        if (!auth.userIsExperimenter()) {
           return "lists/userNotExperimenter";
        }
        model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
        model.addAttribute("addHardware",data);
        return "lists/hardware/addItemForm";
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addHardware") AddHardwareCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");

        if (!auth.userIsExperimenter()) {
            return "lists/userNotExperimenter";
        }

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
            log.debug("Creating new hardware object.");
            hardware = new Hardware();
            hardware.setTitle(data.getTitle());
            hardware.setType(data.getType());
            hardware.setDescription(data.getDescription());
            hardwareDao.create(hardware);
        }


        log.debug("Returning MAV");
        return "redirect:list.html";
    }
}
