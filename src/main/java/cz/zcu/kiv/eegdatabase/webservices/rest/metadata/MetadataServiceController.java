package cz.zcu.kiv.eegdatabase.webservices.rest.metadata;

import cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers.OdmlWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * MetadataServiceController, 2018/09/04 11:11 petr-jezek
 *
 **********************************************************************************************************************/
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/metadata")
public class MetadataServiceController {

    @Autowired
    MetadataService metadataService;

    /**
     * Get odml metadata for experiment.
     *
     * @param experimentId id of experiment
     * @return metadata for experiment
     */
    @RequestMapping(value = "/public/{experimentId}", method = RequestMethod.GET, produces={"application/xml;charset=UTF-8"})
    public OdmlWrapper getPublicExperiments(@PathVariable int experimentId) {
        return metadataService.getOdml(experimentId);
    }
}
