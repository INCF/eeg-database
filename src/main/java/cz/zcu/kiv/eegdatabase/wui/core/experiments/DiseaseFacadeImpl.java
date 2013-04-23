package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 10:28
 */
public class DiseaseFacadeImpl implements DiseaseFacade {
    DiseaseService diseaseService;

    @Required
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @Override
    public Integer create(Disease newInstance) {
        return diseaseService.create(newInstance);
    }

    @Override
    public boolean existsDisease(String name) {
        return diseaseService.existsDisease(name);
    }
}
