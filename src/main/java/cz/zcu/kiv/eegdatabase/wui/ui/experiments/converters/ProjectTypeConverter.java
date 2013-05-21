package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 12.5.13
 * Time: 15:18
 */
public class ProjectTypeConverter implements IConverter<ProjectType> {
    private ProjectTypeFacade projectTypeFacade;

    public ProjectTypeConverter(ProjectTypeFacade projectTypeFacade){
        this.projectTypeFacade = projectTypeFacade;
    }

    @Override
    public ProjectType convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        ProjectType projectType = new ProjectType();
        projectType.setTitle(s);
        List<ProjectType> projectTypes = projectTypeFacade.getUnique(projectType);
        return (projectTypes != null && projectTypes.size() > 0) ? projectTypes.get(0) : projectType;

    }

    @Override
    public String convertToString(ProjectType projectType, Locale locale) {
        return projectType.getAutoCompleteData();
    }
}
